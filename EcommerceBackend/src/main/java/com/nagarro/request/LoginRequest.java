package com.nagarro.request;

import lombok.Data;

@Data
public class LoginRequest {
	private String username;
	private String password;
	private String role;
}
