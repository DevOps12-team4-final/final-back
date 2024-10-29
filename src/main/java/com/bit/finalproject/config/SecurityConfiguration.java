package com.bit.finalproject.config;

import com.bit.finalproject.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

@Configuration
// Spring Security의 웹 보안을 활성화합니다.
@EnableWebSecurity
@RequiredArgsConstructor

// CSRF 보호를 비활성화하고, 세션을 사용하지 않으며, 특정 경로에 대한 접근 권한을 설정한다.
// Jwt 필터를 추가해 무상태 방식의 인증을 지원하고, 비밀번호 인코더를 BCryptPasswordEncoder로 설정한다.
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // password를 DB에 저장하기전에 암호화하기위해 사용한다.
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                // Spring에서는 기본적으로 외부 도메인에서의 API요청을 차단하기 때문에
                // 다른 도메인에서 서버를 요청할 수 있도록 허용하려면 CORS(Cross-Origin Resource Sharing)
                // 설정이 필요하다.
                .cors(httpSecurityCorsConfigurer -> {
                })
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(httpSecurityHttpBasicConfigurer -> {
                    httpSecurityHttpBasicConfigurer.disable();
                })
                .sessionManagement(httpSecuritySessionManagementConfigurer -> {
                    httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
                    // 아래의 경로는 인증없이 누구나 접근할 수 있게 설정한다.
                    authorizationManagerRequestMatcherRegistry.requestMatchers(
                            "/users/email-check",
                            "/users/nickname-check",
                            "/users/join",
                            "/users/login",
                            "/room",
                            "/ws",
                            "/ws/**",
                            "/room/**",
                            "/users/tel-check",
                            "/users/send",
                            "/users/modify-password",
                            "/feed",
                            "/feeds/**",
                            "/feed-comment",
                            "/feed-comment/feed/**",
                            "/notifications/**",
                            "/feeds/search").permitAll();

                    // 나머지 요청은 인증이 필요하도록 설정한다.
                    authorizationManagerRequestMatcherRegistry.anyRequest().authenticated();
                })
                // Jwt 토큰을 이용해 인증처리를 할 수 있게 필터 체인에 등록한다.
                .addFilterAt(jwtAuthenticationFilter, CorsFilter.class)
                .build();
    }
}
