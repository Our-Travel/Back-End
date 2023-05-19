package com.example.ot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Getter
    private static ApplicationContext context;
    @Getter
    private static long MAX_FILE_SIZE;
    @Autowired
    public void setContext(ApplicationContext context) {
        AppConfig.context = context;
    }

    @Value("${custom.file.maxFileSize}")
    public void setLikeablePersonModifyCoolTime(long MAX_FILE_SIZE) {
        AppConfig.MAX_FILE_SIZE = MAX_FILE_SIZE;
    }

    // access토큰 시간 관련 오류 해결.
    // LocalDateTime 직렬화 역직렬화 오류 해결.
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().registerModule(new JavaTimeModule());
    }
}

