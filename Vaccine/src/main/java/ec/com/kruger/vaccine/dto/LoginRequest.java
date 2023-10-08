/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.kruger.vaccine.dto;

import lombok.Builder;
import lombok.Data;
/**
 *
 * @author Sebastian LG
 */

@Data
@Builder
public class LoginRequest {
    
    private String username;
    private String password;
}
