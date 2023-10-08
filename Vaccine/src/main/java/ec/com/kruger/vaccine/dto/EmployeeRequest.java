/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.kruger.vaccine.dto;

import java.util.Date;
import lombok.Data;
/**
 *
 * @author Sebastian LG
 */
@Data
public class EmployeeRequest {
    
    private String identification;
    private String names;
    private String lastnames;
    private String email; 
    private Date birthday;
    private String homeAddress;
    private String cellPhone;
    private Boolean vaccinationStatus;
}
