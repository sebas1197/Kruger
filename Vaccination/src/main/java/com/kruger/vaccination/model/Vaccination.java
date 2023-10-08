package com.kruger.vaccination.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vaccinations")
@NoArgsConstructor
@Builder
@Data
public class Vaccination {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Integer id;
	
	@Column(name = "date", nullable = false)
	private LocalDate date;
	
	@Column(name = "dose_numbers", nullable = false)
	private Integer doseNumbers;
	
	
	@JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
	@ManyToOne(optional = false)
	private Employee employee;

}
