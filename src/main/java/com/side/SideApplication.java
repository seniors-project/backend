package com.side;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SideApplication {

	public static void main(String[] args) {
		SpringApplication.run(SideApplication.class, args);
	}

}
