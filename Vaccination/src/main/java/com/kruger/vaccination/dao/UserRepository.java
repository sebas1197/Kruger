package com.kruger.vaccination.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kruger.vaccination.model.Rol;
import com.kruger.vaccination.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	List<User> findByRole(Rol rol);
	List<User> findByUsername(String username);
}
