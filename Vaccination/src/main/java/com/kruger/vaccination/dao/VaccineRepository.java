/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.kruger.vaccination.dao;

import com.kruger.vaccination.model.Vaccination;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author papic
 */
public interface VaccineRepository extends JpaRepository<Vaccination, Integer>{
    List<Vaccination> findByTypeVaccine(String typeVaccine);
    List<Vaccination> findByDates(LocalDate start, LocalDate end);
}
