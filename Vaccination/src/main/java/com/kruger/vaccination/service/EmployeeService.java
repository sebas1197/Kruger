/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kruger.vaccination.service;

import com.kruger.vaccination.dao.EmployeeRepository;
import com.kruger.vaccination.dao.VaccineRepository;
import com.kruger.vaccination.util.Validation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.kruger.vaccination.model.Employee;
import com.kruger.vaccination.model.User;
import com.kruger.vaccination.model.Vaccination;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author papic
 */
@Service
@Slf4j
public class EmployeeService {

    @Autowired(required = false)
    private EmployeeRepository employeeRepository;

    @Autowired(required = false)
    private VaccineRepository  vaccineRepository;
    
    private Validation validation = new Validation();

    public List<Employee> getAll() throws Exception{
        
        if(this.employeeRepository.findAll().isEmpty()){
            throw new Exception("Sin registros");
        }
        
        return this.employeeRepository.findAll();
    }

    public Employee getEmployeeByIdentification(String identification) throws Exception {

        if (!validation.validateIdentification(identification)) {
            throw new Exception("Identificicación incorrecta");
        }

        Employee employee = this.employeeRepository.findByIdentification(identification);

        if (employee == null) {
            throw new Exception("Empleado no encontrado");
        }

        return employee;
    }

    @Transactional
    public User createEmployee(Employee employee) throws Exception {

        if (!validation.validateIdentification(employee.getIdentification())) {
            throw new Exception("Identificación incorrecta");
        }

        if (this.employeeRepository.existsByIdentification(employee.getIdentification())) {
            throw new Exception("Identificación ya registrada");
        }

        if (!validation.validateEmail(employee.getEmail())) {
            throw new Exception("Correo electrónico incorrecto");
        }

        if (this.employeeRepository.existsByEmail(employee.getEmail())) {
            throw new Exception("Correo electrónico ya registrado");
        }

        if (!validation.validateOnlyLetters(employee.getNames())) {
            throw new Exception("Nombres incorrectos");
        }

        if (!validation.validateOnlyLetters(employee.getSurnames())) {
            throw new Exception("Apellidos incorrectos");
        }

        Employee employeeCreated = Employee.builder()
                .identification(employee.getIdentification())
                .names(employee.getNames().toUpperCase())
                .surnames(employee.getSurnames().toUpperCase())
                .email(employee.getEmail())
                .build();

        Employee newEmployee = this.employeeRepository.saveAndFlush(employeeCreated);

        UserService userService = new UserService();
        log.info("EMPLEADO CREADO {}", employeeCreated.getId());
        return userService.createUser(newEmployee);
    }

    @Transactional
    public void updateEmployee(Integer id,Employee employee) throws Exception {
        if (!validation.validatePhone(employee.getCellphone())) {
            throw new Exception("Teléfono incorrecto");
        }

        if (this.employeeRepository.existsByCellphone(employee.getCellphone())) {
            throw new Exception("Teléfono ya registrado");
        }
        
        Employee employeeUpdated = this.employeeRepository.findById(id).get();
        employeeUpdated.setBirthdate(employee.getBirthdate());
        employeeUpdated.setHomeAddress(employee.getHomeAddress());
        employeeUpdated.setCellphone(employee.getCellphone());
        employeeUpdated.setVaccinationStatus(employee.getVaccinationStatus());
        
        if(employeeUpdated.getVaccinationStatus()){
            for(Vaccination vaccination: employee.getVaccionations()){
                Vaccination newVaccination = new Vaccination();
                newVaccination.setEmployee(employee);
                newVaccination.setDate(vaccination.getDate());
                newVaccination.setDoseNumbers(vaccination.getDoseNumbers());
                newVaccination.setTypeVaccinate(vaccination.getTypeVaccinate());
                employeeUpdated.getVaccionations().add(newVaccination);
                this.vaccineRepository.save(newVaccination);
            }
        }
        log.info("EMPLEADO EDITADO {}", employeeUpdated.getId());
        this.employeeRepository.save(employeeUpdated);
    }

    @Transactional
    public void deleteEmployee(Integer id) throws Exception {

        if (!this.employeeRepository.existsById(id)) {
            throw new Exception("Empleado no encontrado");
        }
        Employee employee = this.employeeRepository.findById(id).get();
        UserService userService = new UserService();
        userService.deleteUser(employee);
        log.info("EMPLEADO BORRADO {}", employee.getId());
        this.employeeRepository.deleteById(id);
    }
    
    public List<Employee> findByVaccinationStatus(Boolean vaccinationStatus) throws Exception{
        
        if(this.employeeRepository.findByVaccinationStatus(vaccinationStatus).isEmpty()){
            throw new Exception("No se encontraron registros");
        }
        
        return this.employeeRepository.findByVaccinationStatus(vaccinationStatus);
    }
    
    public List<Employee> findByTypeVaccine(String typeVaccine) throws Exception{
        
        if(this.vaccineRepository.findByTypeVaccine(typeVaccine).isEmpty()){
            throw new Exception("No se encontraron registros");
        }
        List<Vaccination> vaccines = this.vaccineRepository.findByTypeVaccine(typeVaccine);
        List<Employee> employees = new ArrayList<>();
        
        for(Vaccination vaccine: vaccines){
            if(!employees.contains(vaccine.getEmployee())){
                employees.add(vaccine.getEmployee());
            }
        }
        
        return employees;
    }
    
    public List<Employee> findByDates(LocalDate start, LocalDate end)throws Exception{
        
        if(this.vaccineRepository.findByDates(start, end).isEmpty()){
            throw new Exception("No se encontraron registros");
        }
        
        List<Vaccination> vaccines = this.vaccineRepository.findByDates(start, end);
        List<Employee> employees = new ArrayList<>();
        
        for(Vaccination vaccine: vaccines){
            if(!employees.contains(vaccine.getEmployee())){
                employees.add(vaccine.getEmployee());
            }
        }
        
        return employees;
    } 
}













