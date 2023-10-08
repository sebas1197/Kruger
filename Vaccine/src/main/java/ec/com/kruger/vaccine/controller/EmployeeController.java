/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.kruger.vaccine.controller;

import ec.com.kruger.vaccine.dto.EmployeeRequest;
import ec.com.kruger.vaccine.dto.DatesRequest;
import ec.com.kruger.vaccine.dto.LoginRequest;
import ec.com.kruger.vaccine.dto.DataEmployeeRequest;
import ec.com.kruger.vaccine.model.Employee;
import ec.com.kruger.vaccine.model.Rol;
import ec.com.kruger.vaccine.security.JWTAuthorizationFilter;
import ec.com.kruger.vaccine.services.EmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author Sebastian LG
 */
//@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/employee")
@Slf4j
@Api(tags = "Employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    private JWTAuthorizationFilter jwtAuthorization = new JWTAuthorizationFilter();

    @PostMapping
    @ApiOperation(value = "Registrar un nuevo empleado",
            notes = "Solo el administrador puede regisrar un empleado")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Ok"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 500, message = "Internal Server Error")})

    public ResponseEntity registerEmployee(@RequestBody EmployeeRequest employeeRequest, @RequestHeader("Authorization") String jwt) {
        try {

            if (!this.jwtAuthorization.validateJwt(jwt)) {
                return ResponseEntity.badRequest().body("JWT INCORRECTO");
            }

            if (!this.jwtAuthorization.getRoleFromJwt(jwt).equals(Rol.ADMINISTRADOR.toString())) {
                return ResponseEntity.badRequest().body("Solo los administradores pueden ejecutar esta acción");
            }

            LoginRequest generatedCredentials = this.employeeService.registerEmployee(employeeRequest);
            return ResponseEntity.ok(generatedCredentials);

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
    public ResponseEntity getAllEmployees(@RequestHeader("Authorization") String jwt) {
        try {

            if (!this.jwtAuthorization.validateJwt(jwt)) {
                return ResponseEntity.badRequest().body("JWT INCORRECTO");
            }

            if (!this.jwtAuthorization.getRoleFromJwt(jwt).equals(Rol.ADMINISTRADOR.toString())) {
                return ResponseEntity.badRequest().body("Solo los administradores pueden ejecutar esta acción");
            }

            List<Employee> employees = this.employeeService.getAllEmployees();
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "Listar empleado por su ID",
            notes = "Coincidencia por ID del empleado")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Ok"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 500, message = "Internal Server Error")})
    public ResponseEntity getEmployeeById(@PathVariable Integer id, @RequestHeader("Authorization") String jwt) {
        try {

            if (!this.jwtAuthorization.validateJwt(jwt)) {
                return ResponseEntity.badRequest().body("JWT INCORRECTO");
            }

            Employee employee = this.employeeService.getEmployeeById(id);
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
    public ResponseEntity updateEmployee(@PathVariable Integer id, @RequestBody DataEmployeeRequest dataEmployeeRequest, @RequestHeader("Authorization") String jwt) {
        try {

            if (!this.jwtAuthorization.validateJwt(jwt)) {
                return ResponseEntity.badRequest().body("JWT INCORRECTO");
            }

            this.employeeService.updateEmployee(id, dataEmployeeRequest, this.jwtAuthorization.getRoleFromJwt(jwt));
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
    public ResponseEntity deleteEmployee(@PathVariable Integer id, @RequestHeader("Authorization") String jwt) {
        try {

            if (!this.jwtAuthorization.validateJwt(jwt)) {
                return ResponseEntity.badRequest().body("JWT INCORRECTO");
            }

            if (!this.jwtAuthorization.getRoleFromJwt(jwt).equals(Rol.ADMINISTRADOR.toString())) {
                return ResponseEntity.badRequest().body("Solo los administradores pueden ejecutar esta acción");
            }

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
    public ResponseEntity findByVaccinationStatus(@PathVariable Boolean status, @RequestHeader("Authorization") String jwt) {
        try {

            if (!this.jwtAuthorization.validateJwt(jwt)) {
                return ResponseEntity.badRequest().body("JWT INCORRECTO");
            }

            if (!this.jwtAuthorization.getRoleFromJwt(jwt).equals(Rol.ADMINISTRADOR.toString())) {
                return ResponseEntity.badRequest().body("Solo los administradores pueden ejecutar esta acción");
            }

            List<Employee> employees = this.employeeService.findByVaccinationStatus(status);
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            log.info("{}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping(value = "/filter/vaccination/dates")
    @ApiOperation(value = "Filtro por rango de fecha",
            notes = "Filtración por rango de fecha de vacunación")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Ok"),
        @ApiResponse(code = 500, message = "Internal Server Error")})
    public ResponseEntity findByDates(@RequestBody DatesRequest datesRequest, @RequestHeader("Authorization") String jwt) {
        try {

            if (!this.jwtAuthorization.validateJwt(jwt)) {
                return ResponseEntity.badRequest().body("JWT INCORRECTO");
            }

            if (!this.jwtAuthorization.getRoleFromJwt(jwt).equals(Rol.ADMINISTRADOR.toString())) {
                return ResponseEntity.badRequest().body("Solo los administradores pueden ejecutar esta acción");
            }

            List<Employee> employees = this.employeeService.findByDates(datesRequest.getStart(), datesRequest.getEnd());
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/filter/vaccine/type/{type}")
    @ApiOperation(value = "Filtro por vacuna",
            notes = "Filtración en base al tipo de vacuna")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Ok"),
        @ApiResponse(code = 500, message = "Internal Server Error")})
    public ResponseEntity findByVaccineType(@PathVariable String type, @RequestHeader("Authorization") String jwt) {
        try {

            if (!this.jwtAuthorization.validateJwt(jwt)) {
                return ResponseEntity.badRequest().body("JWT INCORRECTO");
            }

            if (!this.jwtAuthorization.getRoleFromJwt(jwt).equals(Rol.ADMINISTRADOR.toString())) {
                return ResponseEntity.badRequest().body("Solo los administradores pueden ejecutar esta acción");
            }

            List<Employee> employees = this.employeeService.findByVaccineType(type);
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
