package com.example.AirBnb_Clone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AirBnbCloneApplication {

	public static void main(String[] args) {
		SpringApplication.run(AirBnbCloneApplication.class, args);
	}

}
