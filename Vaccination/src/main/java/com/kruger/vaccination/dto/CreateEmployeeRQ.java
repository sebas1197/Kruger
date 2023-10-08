package com.kruger.vaccination.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CreateEmployeeRQ {

	private String identification;
	private String names;
	private String surnames;
	private String email;
	private LocalDate birthdate;
	private String homeAddress;
	private String cellphone;
	private Boolean vaccinationStatus;
	
}
