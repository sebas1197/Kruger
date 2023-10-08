/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.kruger.vaccination.dao;

import com.kruger.vaccination.model.Employee;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author papic
 */
public interface EmployeeRepository extends JpaRepository<Employee, Integer>{
    	Employee findByIdentification(String identification);
	Boolean existsByEmail(String email);
	Boolean existsByIdentification(String Identification);
	Boolean existsByCellphone(String cellphone);
	Boolean deleteByIdentification(String identification);
	List<Employee> findByVaccinationStatus(Boolean vaccinationStatus);
}
