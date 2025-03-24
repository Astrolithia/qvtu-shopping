package com.qvtu.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI qvtuShoppingOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("QVTU Shopping API")
                        .description("QVTU Shopping Backend API Documentation")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("QVTU Team")
                                .email("contact@qvtu.example.com"))
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .servers(List.of(
                        new Server().url("http://localhost:8080/api").description("Local Development Server")
                ))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .name("bearerAuth")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
} 