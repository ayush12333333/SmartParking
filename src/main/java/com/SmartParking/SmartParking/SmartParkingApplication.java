package com.SmartParking.SmartParking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.SmartParking.SmartParking.repository")
@EntityScan(basePackages = "com.SmartParking.SmartParking.entity")
@ComponentScan(basePackages = "com.SmartParking.SmartParking")
public class SmartParkingApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartParkingApplication.class, args);
	}

}
