package com.kruger.vaccination.dao;

import java.util.List;

import com.kruger.vaccination.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Integer>{
	Employee findByIdentification(String identification);
	Boolean existsByEmail(String email);
	Boolean existsByIdentification(String Identification);
	Boolean existsByCellphone(String cellphone);
	Boolean deleteByIdentification(String identification);
	List<Employee> findByVaccinationStatus(Boolean vaccinationStatus);
}
