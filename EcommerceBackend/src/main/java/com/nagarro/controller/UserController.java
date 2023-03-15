package com.nagarro.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nagarro.model.User;
import com.nagarro.response.ResponseHandler;
import com.nagarro.service.UserService;
import com.nagarro.utils.ImageUtil;

@CrossOrigin
@RestController
@RequestMapping("/ecommerce")
public class UserController {
	@Autowired
	UserService userService;
	
	@Autowired
	ImageUtil imageUtil;
	
	@GetMapping(value = "/user/{username}")
	public ResponseEntity<Object> getUser(@PathVariable String username) {
	try {
		Optional<User> userFromDb = userService.findByUsername(username);
		if(!userFromDb.isPresent()) {
			return ResponseHandler.generateResponse("User Not Found", HttpStatus.NOT_FOUND,null);
		}
		User user = userFromDb.get();
		
		user.setUserImage(imageUtil.decompressBytes(userFromDb.get().getUserImage()));
		
		return ResponseHandler.generateResponse("User Details Retrieved Successfully", HttpStatus.OK,user);
	}
	
	catch(Exception e) {
		e.printStackTrace();
		return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
	}
	
	}
}
