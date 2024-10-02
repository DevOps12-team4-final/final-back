package com.bit.finalproject.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Spring Security에서 Jwt를 이용한 인증처리를 하기 위한 필터로,
// 매 요청마다 Jwt 유효성을 검사하고, 유효한 토큰일 경우 해당 사용자를 인증된 상태로 만들어준다.
// OncePerRequestFilter를 상속받아 요청마다 한번씩 실행되고, doFilterInternal를 오버라이드하여 로직을 구현한다.

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Jwt토큰을 생성하고 유효성을 검사하는 역할의 클래스이다.
    private final JwtProvider jwtProvider;
    // Spring Security에서 제공하는 interface로 DB에서 사용자 정보를 가져오는데 사용된다.
    private final UserDetailsService userDetailsService;

    private String parseBearerToken(HttpServletRequest request) {

        // HttpServletRequest의 getHeader 메서드를 호출해서 Authorization이름의 헤더값을 가져온다.
        // Authorization은 클라이언트가 서버로 인증 정보를 보낼 때 사용하는 표준 헤더이다.
        // 형식 -> Authorization: Bearer <JWT 토큰>
        String bearerToken = request.getHeader("Authorization");

        // null, "", " " -> 빈 값인지 체크한다.
        // bearerToken이 Bearer로 시작하는지 체크한다.
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            // "Bearer " 이후 부분을 추출한다.
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            // http요청 헤더에서 Jwt 토큰을 추출한다.
            String token = parseBearerToken(request);
            log.info("Parsed JWT Token: {}", token);  // JWT 토큰이 올바르게 추출되었는지 확인

            if (token != null && !token.equalsIgnoreCase("null")) {
                // 토큰의 유효성을 검사하고, 유효하다면 토큰에서 사용자이름(Email)을 추출한다.
                String Email = jwtProvider.validatedAndGetSubject(token);
                log.info("Email from JWT Token: {}", Email);  // 토큰에서 추출한 사용자 이름 로그 출력

                // DB에서 Email에 해당하는 사용자 정보를 로드한다.
                // Spring Security가 인증 정보를 관리할 수 있도록 UserDetails 객체로 가져온다.
                UserDetails userDetails = userDetailsService.loadUserByUsername(Email);
                log.info("UserDetails loaded: {}", userDetails);  // UserDetails 객체 로그 출력

                // EmailPasswordAuthenticationToken객체를 생성해서 userDetails와 권한정보가 포함한 인증 객체를 생성한다.
                // 이 객체는 Spring Security의 인증 시스템에서 사용되는 표준 인증 객체이다.
                AbstractAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                // authenticationToken에 요청의 세부정보를 추가하고, SecurityContext에 인증 객체에 현재 사용자 인증 정보를 저장한다.
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContext securityContext = SecurityContextHolder.getContext();
                securityContext.setAuthentication(authenticationToken);
                SecurityContextHolder.setContext(securityContext);

                log.info("Authentication token set in SecurityContext for user: {}", Email);
            }
        } catch (ExpiredJwtException e) {
            log.error("JWT Token has expired: {}", e.getMessage());
        } catch (UsernameNotFoundException e) {
            log.error("User not found in database: {}", e.getMessage());
        } catch (JwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error in setting security context: {}", e.getMessage(), e);  // 스택 트레이스 출력
        }

        // 현재 필터에서 다음 필터로 요청과 응답 전달한다.
        filterChain.doFilter(request, response);
    }
}
