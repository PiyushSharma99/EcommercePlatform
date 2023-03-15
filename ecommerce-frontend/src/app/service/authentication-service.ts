import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { LoginForm } from '../model/login-form-model';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

    USER_NAME_LOCAL_SESSION_ATTRIBUTE_NAME = 'authenticatedUser';
    USER_ROLE = 'userRole';
    username: string;
    password: string;
    role: string;
    loginModel:LoginForm;
    constructor(private httpClient: HttpClient,
        private router : Router) {

    }

    authenticationService(loginModel : LoginForm): Observable<any> {
        this.loginModel = loginModel;
        return this.httpClient.post<any>('http://localhost:8080/ecommerce/auth/signin',this.loginModel);
 
    }

    registerSuccessfulLogin() {
        localStorage.setItem(this.USER_NAME_LOCAL_SESSION_ATTRIBUTE_NAME, this.loginModel.username)
        localStorage.setItem(this.USER_ROLE,this.loginModel.role);
        this.username = this.loginModel.username;
        this.password = this.loginModel.username;
    }

    logout() {
        localStorage.removeItem(this.USER_NAME_LOCAL_SESSION_ATTRIBUTE_NAME);
        localStorage.removeItem(this.USER_ROLE);
        this.username = null;
        this.password = null;
        this.role = null;
    }

    isUserLoggedIn() {
        let user = localStorage.getItem(this.USER_NAME_LOCAL_SESSION_ATTRIBUTE_NAME);
        if (user === null) return false;
        return true;
    }

    getRole() {
        let role = localStorage.getItem(this.USER_ROLE);
        if(role === null) return '';
        return role;
    }
    getLoggedInUserName() {
        let user = localStorage.getItem(this.USER_NAME_LOCAL_SESSION_ATTRIBUTE_NAME)
        if (user === null) return '';
        return user;
    }
  
}