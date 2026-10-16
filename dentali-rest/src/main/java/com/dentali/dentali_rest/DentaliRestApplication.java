package com.dentali.dentali_rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class DentaliRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(DentaliRestApplication.class, args);
	}

}
