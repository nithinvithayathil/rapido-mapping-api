package com.ustglobal.rapido.mapping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@ComponentScan({ "com.ustglobal.rapido" })
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
public class BusinessEntityMappingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BusinessEntityMappingApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
