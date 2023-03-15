package com.nagarro.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.lang3.StringUtils;

import com.nagarro.constants.ResponseMessageConstants;
import com.nagarro.model.Role;
import com.nagarro.model.User;
import com.nagarro.request.LoginRequest;
import com.nagarro.request.SignUpRequest;
import com.nagarro.response.ResponseHandler;
import com.nagarro.service.RoleService;
import com.nagarro.service.UserService;
import com.nagarro.utils.ImageUtil;

@CrossOrigin
@RestController
@RequestMapping("/ecommerce/auth")
public class AuthController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	RoleService roleService;
	
	@Autowired 
	ImageUtil imageUtil;
	
	@PostMapping(value = "/signup" ,consumes = {"multipart/form-data"})
	public ResponseEntity<Object> registerUser(@RequestPart("userData") SignUpRequest signUpRequest, @RequestPart("imageFile") MultipartFile file) {
		try {
						
			// generating roles mapped with user
		    Set<String> strRoles = signUpRequest.getRole();
		    Set<Role> roles = new HashSet<>();
		    if(strRoles != null) {
		    	for (String role : strRoles) {
		    		Optional<Role> userRole = roleService.findByName(role);
		    		if(!userRole.isPresent()) {
		    			return ResponseHandler.generateResponse(ResponseMessageConstants.ROLE_IS_NOT_FOUND, HttpStatus.BAD_REQUEST, null);
		    		}
		    		roles.add(userRole.get());
		    	}
		    }
		    
		    
			// to avoid duplicate email and username
			if (userService.existsByUsername(signUpRequest.getUsername())) {
				return ResponseHandler.generateResponse(ResponseMessageConstants.USERNAME_ALREADY_PRESENT, HttpStatus.OK, null);
			}
			if (userService.existsByEmail(signUpRequest.getEmail())) {
				return ResponseHandler.generateResponse(ResponseMessageConstants.EMAIL_ALREADY_PRESENT, HttpStatus.OK, null);
			}
			
			// Create new user's account
		    User user = new User();
		    user.setUsername(signUpRequest.getUsername());
		    user.setEmail(signUpRequest.getEmail());
		    user.setName(signUpRequest.getName());
		    user.setPassword(DigestUtils.sha256Hex(signUpRequest.getPassword()));
		    user.setRoles(roles);
		    user.setUserImage(imageUtil.compressBytes(file.getBytes()));
		    
		    // saving user
		    userService.saveUser(user);
			return ResponseHandler.generateResponse(ResponseMessageConstants.USER_REGISTERED_SUCCESSFULLY, HttpStatus.CREATED, user);
		} catch (HttpClientErrorException e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(ResponseMessageConstants.CONFLICT_MESSAGE, HttpStatus.CONFLICT, null);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
		}

	  }
	
	
	@PostMapping("/signin")
	public ResponseEntity<Object> authenticateUser(@RequestBody LoginRequest loginRequest) {
		try {
			
			// to avoid query in which username is not present
			if (!userService.existsByUsername(loginRequest.getUsername())) {
				return ResponseHandler.generateResponse(ResponseMessageConstants.USERNAME_NOT_PRESENT, HttpStatus.UNAUTHORIZED, null);
			}
			
			// Find user's account
		    Optional<User> user = userService.findByUsername(loginRequest.getUsername());
		    
		    // checking if user is associated to selected role or not
		    Set<Role> userRoles = user.get().getRoles();
		    Set <String> roleNameSet = new HashSet <> ();
		    for(Role role :userRoles) {
		    	roleNameSet.add(role.getName());
		    }
		    
			if (!roleNameSet.contains(loginRequest.getRole())) {
				return ResponseHandler.generateResponse(ResponseMessageConstants.INVALID_ROLE, HttpStatus.UNAUTHORIZED, null);
			}
			
			// checking if password is correct or not
		    String passwordSavedInDb = user.get().getPassword();
		    String loginPassword = DigestUtils.sha256Hex(loginRequest.getPassword());
		    if(StringUtils.equals(passwordSavedInDb, loginPassword)) { 
		    	return ResponseHandler.generateResponse(ResponseMessageConstants.USER_AUTHENTICATED_SUCCESSFULLY, HttpStatus.OK, null);
		    }
		    return ResponseHandler.generateResponse(ResponseMessageConstants.INVALID_PASSWORD, HttpStatus.UNAUTHORIZED, null);
		} catch (HttpClientErrorException e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(ResponseMessageConstants.CONFLICT_MESSAGE, HttpStatus.CONFLICT, null);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
		}

	  }	
		
		@GetMapping(value = "/existEmail/{email}")
		public ResponseEntity<Object> emailExist(@PathVariable String email) {
		try {
			if(userService.existsByEmail(email)) {
				return ResponseHandler.generateResponse(null, HttpStatus.OK, null);
			}
			return ResponseHandler.generateResponse(null, HttpStatus.NOT_FOUND, null);
		}
		catch(Exception e) {
			return ResponseHandler.generateResponse(null, HttpStatus.NOT_FOUND, null);
		}
	}
		
		@GetMapping(value = "/existUsername/{userName}")
		public ResponseEntity<Object> userNamexist(@PathVariable String userName) {
		try {
			if(userService.existsByUsername(userName)) {
				return ResponseHandler.generateResponse(null, HttpStatus.OK, null);
			}
			return ResponseHandler.generateResponse(null, HttpStatus.NOT_FOUND, null);
		}
		catch(Exception e) {
			return ResponseHandler.generateResponse(null, HttpStatus.NOT_FOUND, null);
		}
	}
		
}
