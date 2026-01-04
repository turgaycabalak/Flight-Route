package com.flight.routes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FlightRouteServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlightRouteServiceApplication.class, args);
	}

}
