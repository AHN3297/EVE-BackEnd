package com.kh.evision.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 모든 경로에 대해
                .allowedOrigins("http://localhost:5175", "http://localhost:5173", "http://localhost:5175")  // 프론트엔드 주소
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")  // 허용 메소드
                .allowedHeaders("*")  // 모든 헤더 허용
                .allowCredentials(true)  // 쿠키/인증 정보 허용
                .maxAge(3600);  // preflight 요청 캐시 시간
    }

}
    
