/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kruger.vaccination.service;

import com.kruger.vaccination.dao.UserRepository;
import com.kruger.vaccination.model.Employee;
import com.kruger.vaccination.model.Rol;
import com.kruger.vaccination.model.User;
import com.kruger.vaccination.security.Encode;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author papic
 */
@Service
@Slf4j
public class UserService {

    @Autowired(required = false)
    private UserRepository userRepository;

    @Transactional
    public User createUser(Employee employee) throws Exception {

        String username = employee.getNames().substring(0, 3) + employee.getId();
        String password = username + Integer.parseInt(employee.getIdentification().substring(3, 6));

        Encode encode = new Encode();

        User userCreated = User.builder()
                .username(username.toUpperCase())
                .password(encode.encrypt(password))
                .role(Rol.EMPLOYEE.toString())
                .employee(employee)
                .build();
        
        log.info("USUARIO CREADO: {}", userCreated.getId());
        return this.userRepository.save(userCreated);
    }

    public List<User> getAll() {
        return this.userRepository.findAll();
    }

    @Transactional
    public void deleteUser(Employee employee) throws Exception {
        if (!this.userRepository.existsByEmployee(employee)) {
            throw new Exception("Empleado no encontrado");
        }
        User user = new User();
        user = this.userRepository.findByEmployee(employee);
        log.info("USUARIO BORRADO {}", user.getId());
        this.userRepository.deleteById(user.getId());
    }
}
