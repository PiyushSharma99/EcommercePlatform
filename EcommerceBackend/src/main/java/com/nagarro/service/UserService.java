package com.nagarro.service;

import java.util.List;
import java.util.Optional;

import com.nagarro.model.User;
import com.nagarro.model.Role;

public interface UserService {
	Optional<User> findByUsername(String userName);
	User saveUser(User user);
	Boolean existsByUsername(String userName);
	Boolean existsByEmail(String email);
}
