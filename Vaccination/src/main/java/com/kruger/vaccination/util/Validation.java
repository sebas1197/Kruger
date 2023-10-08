/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kruger.vaccination.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author papic
 */
public class Validation {

    public Validation() {
    }
    
    
    
    public boolean validateIdentification(String identification) {
        if (identification.length() != 10) {
            return false;
        }
        int sum = 0;
        int num = 0;
        int validationNum = 0;
        for (int i = 0; i < 9; i++) {
            if (i % 2 == 0) {
                num = Integer.parseInt(identification.substring(i, i + 1)) * 2;
            } else {
                num = Integer.parseInt(identification.substring(i, i + 1));
            }
            if (num >= 10) {
                num += -9;
            }
            sum += num;
        }
        validationNum = (((sum / 10) + 1) * 10) - sum;
        return validationNum == Integer.parseInt(identification.substring(9));
    }
	
    public boolean validateEmail(String mail) {
        Pattern pat = Pattern.compile("([a-z0-9]+(\\.?[a-z0-9])*)+@(([a-z]+)\\.([a-z])+(.[a-z]+))");
        Matcher mather = pat.matcher(mail);
        return mather.find();
    }
    
    public boolean validateOnlyLetters(String letters) {
        Pattern pat = Pattern.compile("([A-Za-zÀ-ÿ\\u00f1\\u00d1]+)( )([A-Za-zÀ-ÿ\\u00f1\\u00d1]+)");
        Matcher matherLetters = pat.matcher(letters);
        return matherLetters.find();
    }
    
    public boolean validatePhone(String phone){
        if(phone.length()!=10){
            return false;
        }
        Pattern pat = Pattern.compile("[0-9]");
        Matcher mather = pat.matcher(phone);
        return mather.find();
    }
}
