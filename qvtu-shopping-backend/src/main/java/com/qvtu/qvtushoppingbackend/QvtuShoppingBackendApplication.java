package com.qvtu.qvtushoppingbackend;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
@SpringBootApplication
@EntityScan("com.qvtu.model")
@EnableJpaRepositories("com.qvtu.repository")
@ComponentScan(basePackages = {"com.qvtu"})
@OpenAPIDefinition
public class QvtuShoppingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(QvtuShoppingBackendApplication.class, args);
	}

}
