package com.bit.finalproject.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
// Spring에서 CORS 설정을 위한 구성 클래스이다.
public class WebMvcConfiguration implements WebMvcConfigurer {

    // Spring MVC에 CORS 규칙을 추가한다.
    // CorsRegistry 객체를 사용해 허용할 URL경로, 메소드, 헤더 등을 정의한다.
    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        // 모든 URL경로에 대해 CORS 규칙을 적용한다는 의미이다.
        corsRegistry.addMapping("/**")
                // "특정 출처"를 허용한다. (리액트 포트)
                .allowedOrigins("http://localhost:3000")
                // "GET", "POST", "PUT", "DELETE", "PATCH" 요청에 대해서만 CORS정책이 적용된다.
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                // 클라이언트가 요청에 포함할 수 있는 HTTP헤더를 정의한다.
                // "*"로 설정하면 모든 헤더를 허용한다는 의미이다.
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);

    }


}
