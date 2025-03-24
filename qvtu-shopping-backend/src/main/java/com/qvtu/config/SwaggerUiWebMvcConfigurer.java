package com.qvtu.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SwaggerUiWebMvcConfigurer implements WebMvcConfigurer {

    private final String baseUrl = "/api";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String baseUrl = StringUtils.trimTrailingCharacter(this.baseUrl, '/');
        
        // Swagger UI resources
        registry.addResourceHandler(baseUrl + "/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/swagger-ui/4.15.5/");
        
        // Webjars resources
        registry.addResourceHandler(baseUrl + "/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
                
        // 添加通用 Swagger 配置
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/swagger-ui/4.15.5/");
                
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController(baseUrl + "/swagger-ui/")
                .setViewName("forward:" + baseUrl + "/swagger-ui/index.html");
        registry.addViewController("/swagger-ui/")
                .setViewName("forward:/swagger-ui/index.html");
    }
} 