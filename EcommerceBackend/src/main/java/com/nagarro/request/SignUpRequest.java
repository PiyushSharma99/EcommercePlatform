package com.nagarro.request;

import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data 
public class SignUpRequest {
    private String username;
    private String name;
    private String email;
    
    private Set<String> role;
    
    private String password;
//    private MultipartFile userImage;
}
