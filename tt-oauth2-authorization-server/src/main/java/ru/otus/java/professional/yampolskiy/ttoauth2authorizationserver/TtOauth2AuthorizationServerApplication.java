package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TtOauth2AuthorizationServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TtOauth2AuthorizationServerApplication.class, args);
	}

}
