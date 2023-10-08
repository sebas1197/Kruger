package com.kruger.vaccination.model;

import java.io.Serializable;

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
@Table(name = "users")
@NoArgsConstructor
@Builder
@Data
public class User implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Integer id;
	
	@Column(name = "username", length = 20, nullable = false)
	private String username;
	
	@Column(name = "password", length = 8, nullable = false)
	private String password;
	
	@Column(name = "role", length = 20, nullable = false)
	private String role;
	
	@JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
	@ManyToOne(optional = false)
	private Employee employee;
	
}
