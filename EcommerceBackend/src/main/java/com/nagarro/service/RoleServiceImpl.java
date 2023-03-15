package com.nagarro.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nagarro.model.Role;
import com.nagarro.repository.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService{

	@Autowired 
	RoleRepository roleRepository;
	
	@Override
	public Optional<Role> findByName(String role) {
		return roleRepository.findByName(role);
	}

	@Override
	public List<Role> findAll() {
		return roleRepository.findAll();
	}
	

}
