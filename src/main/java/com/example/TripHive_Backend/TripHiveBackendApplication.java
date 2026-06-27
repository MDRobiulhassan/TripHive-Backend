package com.example.TripHive_Backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TripHiveBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(TripHiveBackendApplication.class, args);
	}

}
