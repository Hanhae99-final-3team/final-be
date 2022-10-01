package com.hanghae.mungnayng;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@EnableCaching
@EnableAspectJAutoProxy(exposeProxy=true)
@EnableScheduling
@SpringBootApplication
public class MungNayngApplication {

	public static void main(String[] args) {
		SpringApplication.run(MungNayngApplication.class, args);
	}

}
