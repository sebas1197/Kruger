package com.kruger.vaccination.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kruger.vaccination.dao.EmployeeRepository;
import com.kruger.vaccination.model.Employee;
import com.kruger.vaccination.util.Validation;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;
	
	
	//@Autowired
	//private VaccinationRepository vaccinationRepository;
	
	private Validation validation = new Validation();
	
	public List<Employee> getAll(){
		return this.employeeRepository.findAll();
	}
	
	public Employee getEmployeeByIdentification(String identification) throws Exception {
		
		if(!validation.validateIdentification(identification)) {
			throw new Exception("Identificicación incorrecta");
		}
		
		Employee employee = this.employeeRepository.findByIdentification(identification);
		
		if(employee == null) {
			throw new Exception("Empleado no encontrado");
		}
		
		return employee;
	}
	
	public List<Employee> getEmployeesByVaccinationStatus(Boolean vaccinationStatus) throws Exception{
		List<Employee> employees = this.employeeRepository.findByVaccinationStatus(vaccinationStatus);
		
		if(employees.isEmpty()) {
			throw new Exception("No se encontraron empleados en el estado " + vaccinationStatus);
		}
		
		return employees;
	}
	
	@Transactional
	public void createEmployee(Employee employee) throws Exception{

		if(!Validation.validateIdentification(employee.getIdentification())) {
			throw new Exception("Identificicación incorrecta");
		}
		
		if(this.employeeRepository.existsByIdentification(employee.getIdentification())) {
			throw new Exception("Identificación ya registrada");
		}
		
		if(!validation.validateEmail(employee.getEmail())) {
			throw new Exception("Correo electrónico incorrecto");
		}
		
		if(this.employeeRepository.existsByEmail(employee.getEmail())) {
			throw new Exception("Correo electrónico ya registrado");
		}
		
		if(!validation.validateEmail(employee.getEmail())) {
			throw new Exception("Correo electrónico incorrecto");
		}
		
		if(!validation.validateOnlyLetters(employee.getNames())) {
			throw new Exception("Nombres incorrectos");
		}
		
		if(!validation.validateOnlyLetters(employee.getSurnames())) {
			throw new Exception("Apellidos incorrectos");
		}
		
		if(!validation.validatePhone(employee.getCellphone())) {
			throw new Exception("Teléfono incorrecto");
		}
		
		if(this.employeeRepository.existsByCellphone(employee.getCellphone())) {
			throw new Exception("Teléfono ya registrado");
		}
		
		Employee employeeCreated = Employee.builder()
				.identification(employee.getIdentification())
				.names(employee.getNames().toUpperCase())
				.surnames(employee.getSurnames().toUpperCase())
				.email(employee.getEmail())
				.birthdate(employee.getBirthdate())
				.homeAddress(employee.getHomeAddress())
				.cellphone(employee.getCellphone())
				.vaccination_status(employee.getVaccinationStatus())
				.build();
		
		Employee newEmployee = this.employeeRepository.saveAndFlush(employeeCreated);
						
		
		if(employeeCreated.getVaccinationStatus()) {
			//TODO: CALL SERVICE VACCINATION
		}
		
		
		
	}
	
	@Transactional
	public void updateEmployee(Employee employee) throws Exception{
		
		if(!this.employeeRepository.existsById(employee.getId())) {
			throw new Exception("Empleado no encontrado");
		}
		
		if(!validation.validateIdentification(employee.getIdentification())) {
			throw new Exception("Identificicación incorrecta");
		}
		
		if(!validation.validateEmail(employee.getEmail())) {
			throw new Exception("Correo electrónico incorrecto");
		}
		
		this.employeeRepository.save(employee);
	}
	
	@Transactional
	public void deleteEmployee(Employee employee) throws Exception{
		
		if(!this.employeeRepository.existsById(employee.getId())) {
			throw new Exception("Empleado no encontrado");
		}
		
		this.employeeRepository.deleteById(employee.getId());
	}
	
}
