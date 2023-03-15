package com.nagarro.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nagarro.constants.ResponseMessageConstants;
import com.nagarro.model.Role;
import com.nagarro.response.ResponseHandler;
import com.nagarro.service.RoleService;

@CrossOrigin
@RestController
@RequestMapping("/ecommerce")
public class RoleController {
		
	@Autowired
	RoleService roleService;
	
	@GetMapping("/roles")
	public ResponseEntity<Object> getAllRoles() {
		try {
			List<Role> authors = roleService.findAll();
			if (authors.isEmpty() || authors.size() == 0) {
				return ResponseHandler.generateResponse(ResponseMessageConstants.NO_DATA_MESSAGE, HttpStatus.NO_CONTENT, null);
			}
			return ResponseHandler.generateResponse(ResponseMessageConstants.SUCCESS_GET_MESSAGE, HttpStatus.OK, authors);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

}
