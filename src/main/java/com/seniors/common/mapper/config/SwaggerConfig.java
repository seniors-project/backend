package com.seniors.common.mapper.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {

    private final ConfigurableEnvironment environment;

    static {
        SpringDocUtils.getConfig()
                .replaceWithClass(LocalDateTime.class, String.class)
                .replaceWithClass(LocalDate.class, String.class)
                .replaceWithClass(LocalTime.class, String.class);
    }

    @Bean
    public OpenAPI getOpenApi() {
        return new OpenAPI()
                .info(getApiInfo());
    }

    private Info getApiInfo() {
        return new Info()
                .title("Seniors API")
                .description("Seniors API 명세서")
                .version("v1");
    }
}