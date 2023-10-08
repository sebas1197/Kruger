/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.kruger.vaccine.controller;

import ec.com.kruger.vaccine.dto.LoginRequest;
import ec.com.kruger.vaccine.dto.LoginResponse;
import ec.com.kruger.vaccine.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
/**
 *
 * @author Sebastian LG
 */
//@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/login")
@Slf4j
@Api(tags = "User")
public class UserController {

    @Autowired
    private UserService userService;
   

    @PostMapping
    @ApiOperation(value = "Login",
            notes = "Login para acceder a las demás consultas")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Ok"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 500, message = "Internal Server Error")})
    public ResponseEntity login(@RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse loginResponse = this.userService.login(loginRequest);
            return ResponseEntity.ok(loginResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
