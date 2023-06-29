package com.seniors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SeniorsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeniorsApplication.class, args);
	}

}
