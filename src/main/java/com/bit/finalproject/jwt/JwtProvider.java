package com.bit.finalproject.jwt;

import com.bit.finalproject.entity.User;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtProvider {

    // base64로 SecretKey 생성
    private static final String SECRET_KEY = "Yml0Y2FtcGRldm9wczEydG9kb2Jvb3RhcHA1MDJyZWFjdHNwcmluZ2Jvb3Q=";

    // SecretKey 생성
    SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    // Jwt 생성 메서드
    public String createJwt(User user) {
        // 유효기간 (1일)
        Date expireDate = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));
        return Jwts.builder()
                // 서명 알고리즘과 비밀 키 설정
                .signWith(key, SignatureAlgorithm.HS256)
                // 주체(사용자 이름)
                .subject(user.getEmail())
                // 발행자
                .issuer("final team project")
                // 발행 시간
                .issuedAt(new Date())
                // 만료 시간
                .expiration(expireDate)
                // 토큰 생성
                .compact();
    }

    // Jwt 검증 및 주체(사용자 이름) 추출
    public String validatedAndGetSubject(String token) {

        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims.getSubject();
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Jwt Token has expired", e);
        } catch (JwtException e) {
            throw new RuntimeException("Invalid Jwt token", e);
        }
    }
}
