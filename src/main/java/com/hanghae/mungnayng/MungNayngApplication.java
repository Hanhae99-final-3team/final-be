package com.hanghae.mungnayng;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MungNayngApplication {

	public static void main(String[] args) {
		SpringApplication.run(MungNayngApplication.class, args);
	}

}
