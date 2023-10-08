package com.kruger.vaccination;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class VaccinationApplication {

	public static void main(String[] args) {
		SpringApplication.run(VaccinationApplication.class, args);
	}

}
