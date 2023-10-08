/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.kruger.vaccine.services;

import ec.com.kruger.vaccine.dao.UserRespository;
import ec.com.kruger.vaccine.dto.LoginRequest;
import ec.com.kruger.vaccine.dto.LoginResponse;
import ec.com.kruger.vaccine.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Sebastian LG
 */

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRespository userRespository;

    public LoginResponse login(LoginRequest loginRequest) throws Exception {

        Optional<User> optionalUser = this.userRespository.findByUsername(loginRequest.getUsername());

        if (optionalUser.isEmpty() || !optionalUser.get().getPassword().equals(loginRequest.getPassword())) {
            throw new Exception("Credenciales incorrectas");
        }

        LoginResponse loginResponse = LoginResponse.builder()
                .username(optionalUser.get().getUsername())
                .role(optionalUser.get().getRole())
                .jwt(generateJwt(optionalUser.get().getUsername(), optionalUser.get().getRole()))
                .id(optionalUser.get().getEmployee().getId())
                .build();

        log.info("INGRESO DE {}", loginResponse.getUsername());
        return loginResponse;
    }

 private String generateJwt(String username, String role) {
     final String SECRET = "Kruger*321";
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }
}
