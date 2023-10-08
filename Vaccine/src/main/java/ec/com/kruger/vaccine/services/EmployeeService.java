/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.kruger.vaccine.services;

import ec.com.kruger.vaccine.dao.EmployeeRepository;
import ec.com.kruger.vaccine.dao.UserRespository;
import ec.com.kruger.vaccine.dao.VaccineTypeRepository;
import ec.com.kruger.vaccine.dto.EmployeeRequest;
import ec.com.kruger.vaccine.dto.LoginRequest;
import ec.com.kruger.vaccine.dto.DataEmployeeRequest;
import ec.com.kruger.vaccine.dto.VaccineRequest;
import ec.com.kruger.vaccine.model.Employee;
import ec.com.kruger.vaccine.model.User;
import ec.com.kruger.vaccine.model.Vaccine;
import ec.com.kruger.vaccine.model.VaccineType;
import ec.com.kruger.vaccine.util.Validation;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ec.com.kruger.vaccine.dao.VaccineRepository;
import ec.com.kruger.vaccine.model.Rol;
import ec.com.kruger.vaccine.security.Encode;

/**
 *
 * @author Sebastian LG
 */
@Service
@Slf4j
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRespository userRespository;

    @Autowired
    private VaccineRepository vaccinationDetailRepository;

    @Autowired
    private VaccineTypeRepository vaccineTypeRepository;

    Validation validation = new Validation();

    public List<Employee> getAllEmployees() throws Exception {

        if (this.employeeRepository.findAll().isEmpty()) {
            throw new Exception("Sin registros");
        }

        return this.employeeRepository.findAll();
    }

    public Employee getEmployeeById(Integer id) throws Exception {

        Optional<Employee> optionalEmployee = this.employeeRepository.findById(id);

        if (optionalEmployee.isEmpty()) {
            throw new Exception("Empleado no encontrado");
        }

        return optionalEmployee.get();
    }

    @Transactional
    public LoginRequest registerEmployee(EmployeeRequest employeeRequest) throws Exception {

        if (!validation.validateIdentification(employeeRequest.getIdentification())) {
            throw new Exception("Identificación incorrecta");
        }

        if (this.employeeRepository.existsByIdentification(employeeRequest.getIdentification())) {
            throw new Exception("Identificación ya registrada");
        }

        if (!validation.validateEmail(employeeRequest.getEmail())) {
            throw new Exception("Correo electrónico incorrecto");
        }

        if (this.employeeRepository.existsByEmail(employeeRequest.getEmail())) {
            throw new Exception("Correo electrónico ya registrado");
        }

        if (!validation.validateOnlyLetters(employeeRequest.getNames())) {
            throw new Exception("Nombres incorrectos");
        }

        if (!validation.validateOnlyLetters(employeeRequest.getLastnames())) {
            throw new Exception("Apellidos incorrectos");
        }

        if(validation.validateNullEmpty(employeeRequest.getNames()) 
                || validation.validateNullEmpty(employeeRequest.getLastnames())
                || validation.validateNullEmpty(employeeRequest.getIdentification())
                || validation.validateNullEmpty(employeeRequest.getEmail())){
   
            throw new Exception("Debe llenar todos los campos");
        }
        
        Employee employee = Employee.builder()
                .names(employeeRequest.getNames().toUpperCase())
                .lastnames(employeeRequest.getLastnames().toUpperCase())
                .identification(employeeRequest.getIdentification())
                .email(employeeRequest.getEmail())
                .build();

        Employee newEmployee = this.employeeRepository.saveAndFlush(employee);

        String username = employeeRequest.getNames().substring(0, 3) + employeeRequest.getIdentification().substring(0, 3);
        String password = username + Integer.parseInt(employeeRequest.getIdentification().substring(3, 6));

        Encode encode = new Encode();

        User user = User.builder()
                .username(username.toUpperCase())
                .password(encode.encrypt(password))
                .role(Rol.EMPLEADO.toString())
                .employee(newEmployee)
                .build();

        log.info("EMPLEADO CREADO: {}", employee.getId());
        log.info("USUARIO CREADO: {}", user.getId());

        this.userRespository.save(user);

        LoginRequest generateCredentials = LoginRequest.builder()
                .username(username)
                .password(encode.encrypt(password))
                .build();

        return generateCredentials;
    }

    public void updateEmployee(Integer id, DataEmployeeRequest dataEmployeeRequest, String role) throws Exception {

        Optional<Employee> optionalEmployee = this.employeeRepository.findById(id);

        if (optionalEmployee.isEmpty()) {
            throw new Exception("Empleado no encontrado");
        }

        Employee employee = optionalEmployee.get();

        if (role.equals(Rol.ADMINISTRADOR.toString())) {
            
                  if(validation.validateNullEmpty(dataEmployeeRequest.getNames()) 
                || validation.validateNullEmpty(dataEmployeeRequest.getLastnames())
                || validation.validateNullEmpty(dataEmployeeRequest.getIdentification())
                || validation.validateNullEmpty(dataEmployeeRequest.getEmail())){
            throw new Exception("Debe llenar todos los campos");
        }
            
            employee.setIdentification(dataEmployeeRequest.getIdentification());
            employee.setNames(dataEmployeeRequest.getNames());
            employee.setLastnames(dataEmployeeRequest.getLastnames());
            employee.setEmail(dataEmployeeRequest.getEmail());
        } else if (role.equals(Rol.EMPLEADO.toString())) {
            
                  if(validation.validateNullEmpty(dataEmployeeRequest.getBirthday()) 
                || validation.validateNullEmpty(dataEmployeeRequest.getHomeAddress())
                || validation.validateNullEmpty(dataEmployeeRequest.getCellPhone())
                || validation.validateNullEmpty(dataEmployeeRequest.getVaccinationStatus())){
            throw new Exception("Debe llenar todos los campos");
        }

            if (!validation.validatePhone(dataEmployeeRequest.getCellPhone())) {
                throw new Exception("Teléfono incorrecto");
            }

            if (!dataEmployeeRequest.getVaccinationStatus() && !dataEmployeeRequest.getVaccinationDetails().isEmpty()) {
                throw new Exception("Usted no ha sido vacunado");
            }

            if (dataEmployeeRequest.getVaccinationStatus() && dataEmployeeRequest.getVaccinationDetails().isEmpty()) {
                throw new Exception("Necesita agregar sus detalles de vacunación");
            }

            employee.setBirthday(dataEmployeeRequest.getBirthday());
            employee.setHomeAddress(dataEmployeeRequest.getHomeAddress());
            employee.setCellPhone(dataEmployeeRequest.getCellPhone());
            employee.setVaccinationStatus(dataEmployeeRequest.getVaccinationStatus());

            if (!dataEmployeeRequest.getVaccinationDetails().isEmpty() && dataEmployeeRequest.getVaccinationStatus()) {
                for (VaccineRequest vaccineRequest : dataEmployeeRequest.getVaccinationDetails()) {

                    Optional<VaccineType> optionalVaccineType = this.vaccineTypeRepository.findById(vaccineRequest.getVaccineType());

                    if (optionalVaccineType.isEmpty()) {
                        throw new Exception("Tipo de vacuna no encontrada");
                    }

                    Vaccine vaccinationDetail = new Vaccine();
                    vaccinationDetail.setEmployee(employee);
                    vaccinationDetail.setVaccinationDate(vaccineRequest.getVaccinationDate());
                    vaccinationDetail.setVaccinationDose(vaccineRequest.getVaccinationDose());
                    vaccinationDetail.setVaccineType(optionalVaccineType.get());

                    log.info("VACUNA REGISTRADA {}", vaccinationDetail.getId());
                    this.vaccinationDetailRepository.save(vaccinationDetail);
                }
            }
        }

        log.info("EMPLEADO EDITADO {}", employee.getId());
        this.employeeRepository.save(employee);

    }

    public void deleteEmployee(Integer id) throws Exception {

        Optional<Employee> optionalEmployee = this.employeeRepository.findById(id);

        if (optionalEmployee.isEmpty()) {
            throw new Exception("Empleado no encontrado");
        }

        Optional<User> optionalUser = this.userRespository.findByEmployee(optionalEmployee.get());

        if (optionalUser.isEmpty()) {
            throw new Exception("Usuario no encontrado");
        }

        log.info("USUARIO BORRADO {}", optionalUser.get().getId());
        log.info("EMPLEADO BORRADO {}", optionalEmployee.get().getId());
        this.userRespository.delete(optionalUser.get());
        this.employeeRepository.delete(optionalEmployee.get());
    }

    public List<Employee> findByVaccinationStatus(Boolean vaccinationStatus) throws Exception {

        if (this.employeeRepository.findByVaccinationStatus(vaccinationStatus).isEmpty()) {
            throw new Exception("Sin registros");
        }

        return this.employeeRepository.findByVaccinationStatus(vaccinationStatus);
    }

    public List<Employee> findByVaccineType(String vaccineType) throws Exception {

        Optional<VaccineType> optionalVaccineType = this.vaccineTypeRepository.findByName(vaccineType);

        if (optionalVaccineType.isEmpty()) {
            throw new Exception("Sin registros");
        }

        List<Vaccine> details = this.vaccinationDetailRepository.findByVaccineType(optionalVaccineType.get());

        List<Employee> employees = new ArrayList<>();

        for (Vaccine vaccine : details) {
            if (!employees.contains(vaccine.getEmployee())) {
                employees.add(vaccine.getEmployee());
            }
        }

        return employees;
    }

    public List<Employee> findByDates(Date start, Date end) throws Exception{
        
        if(!validation.validateDates(start, end)){
            throw new Exception("Fecha final menor a fecha inicial");
        }

        List<Vaccine> details = this.vaccinationDetailRepository.findByVaccinationDateBetween(start, end);

        List<Employee> employees = new ArrayList<>();

        for (Vaccine vaccine : details) {
            if (!employees.contains(vaccine.getEmployee())) {
                employees.add(vaccine.getEmployee());
            }
        }
        return employees;
    }

}
