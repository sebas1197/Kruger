/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kruger.vaccination.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author papic
 */
@Entity
@Table(name = "vaccines")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Vaccination implements Serializable{
    
        @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Integer id;
	
	@Column(name = "date", nullable = false)
	private LocalDate date;
	
	@Column(name = "dose_numbers", nullable = false)
	private Integer doseNumbers;
        
        @Column(name = "type_vaccinate")
        private String typeVaccinate;
	
	@JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
	@ManyToOne(optional = false)
	private Employee employee;
    
}
