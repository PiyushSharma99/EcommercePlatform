package com.nagarro.service;

import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nagarro.model.Role;
import com.nagarro.model.User;
import com.nagarro.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	UserRepository userRepository;
	
	@Override
	public User saveUser(User user) {
		return userRepository.save(user);
	}
	
	public Optional<User> findByUsername(String userName) {
		return userRepository.findByUsername(userName);
	}

	@Override
	public Boolean existsByUsername(String userName) {
		return userRepository.existsByUsername(userName);
	}

	@Override
	public Boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

}
