package com.nagarro.service;

import java.util.List;
import java.util.Optional;

import com.nagarro.model.Role;

public interface RoleService {
	Optional<Role> findByName(String role);
	List<Role> findAll();
}
