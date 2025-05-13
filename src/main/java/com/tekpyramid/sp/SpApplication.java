package com.tekpyramid.sp;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
@EnableWebSecurity
public class SpApplication {
	@PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getDefault());
    }

	public static void main(String[] args) {
		SpringApplication.run(SpApplication.class, args);
	}

}
