package com.university.gradesystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class GradeSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(GradeSystemApplication.class, args);
        System.out.println("=================================");
        System.out.println("🎓 成績管理系統啟動成功!");
        System.out.println("📡 伺服器運行於: http://localhost:3000");
        System.out.println("=================================");
    }

    // CORS 設定
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*");
            }
        };
    }
}