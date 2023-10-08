/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kruger.vaccination.controller;

import com.kruger.vaccination.model.Employee;
import com.kruger.vaccination.model.User;
import com.kruger.vaccination.model.Vaccination;
import com.kruger.vaccination.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author papic
 */
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/employee")
@Slf4j
@Api(tags = "Employee")
public class EmployeeController {
    
     @Autowired
    private EmployeeService employeeService;

    @PostMapping
    @ApiOperation(value = "Registrar un nuevo empleado",
            notes = "Solo el administrador puede regisrar un empleado")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Ok"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 500, message = "Internal Server Error")})
  
    public ResponseEntity createEmployee(@RequestBody Employee employeeRequest) {
        try {
            User user = this.employeeService.createEmployee(employeeRequest);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping
    @ApiOperation(value = "Listar todos los empleados",
            notes = "Lista de empleados registrados")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Ok"),
        @ApiResponse(code = 500, message = "Internal Server Error")})
    public ResponseEntity getAll() {
        try {
            List<Employee> employees = this.employeeService.getAll();
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "Listar empleado por su identificación",
            notes = "Coincidencia por identificación del empleado")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Ok"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 500, message = "Internal Server Error")})
    public ResponseEntity getEmployeeByIdentification(@PathVariable String identification) {
        try {
            Employee employee = this.employeeService.getEmployeeByIdentification(identification);
            return ResponseEntity.ok(employee);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping(value = "/{id}")
    @ApiOperation(value = "Edita empleado por su ID",
            notes = "Editar información de empleado por ID")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Ok"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 500, message = "Internal Server Error")})
    public ResponseEntity updateEmployee(@PathVariable Integer id, @RequestBody Employee employeeRequest) {
        try {
            this.employeeService.updateEmployee(id, employeeRequest);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "Borrar empleado por su ID",
            notes = "Borra un empleado en base a su ID")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Ok"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 500, message = "Internal Server Error")})
    public ResponseEntity deleteEmployee(@PathVariable Integer id) {
        try {
            this.employeeService.deleteEmployee(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping(value = "/filter/status/{status}")
    @ApiOperation(value = "Filtro por estado de vacunación",
            notes = "Filtración en base a su estado de vacunación")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Ok"),
        @ApiResponse(code = 500, message = "Internal Server Error")})
    public ResponseEntity findByVaccinationStatus(@PathVariable Boolean status) {
        try {
            List<Employee> employees = this.employeeService.findByVaccinationStatus(status);
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            log.info("{}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    //@PostMapping(value = "/filter/vaccination/dates")
    //@ApiOperation(value = "Filtro por rango de fecha",
       //     notes = "Filtración por rango de fecha de vacunación")
    //@ApiResponses(value = {
      /*  @ApiResponse(code = 200, message = "Ok"),
        @ApiResponse(code = 500, message = "Internal Server Error")})
    public ResponseEntity findByDates(@RequestBody Vaccination datesRQ) {
        try {
            //List<Employee> employees = this.employeeService.findByDates(datesRQ.get, datesRQ.getEnd());
           // return ResponseEntity.ok(employees);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }*/

    @GetMapping(value = "/filter/vaccine/type/{type}")
    @ApiOperation(value = "Filtro por vacuna",
            notes = "Filtración en base al tipo de vacuna")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Ok"),
        @ApiResponse(code = 500, message = "Internal Server Error")})
    public ResponseEntity findByTypeVaccine(@PathVariable String type) {
        try {
            List<Employee> employees = this.employeeService.findByTypeVaccine(type);
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
}
