package com.accountplace.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ApiApplication {
	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.configure()
				.filename(".env.development")
				.ignoreIfMissing()
				.load();
		dotenv.entries().forEach(entry ->
				System.setProperty(entry.getKey(), entry.getValue())
		);
		SpringApplication.run(ApiApplication.class, args);
	}
}