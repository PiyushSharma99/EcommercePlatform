import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from "@angular/router";
import { AuthenticationService } from "./authentication-service";

@Injectable({
    providedIn: 'root'
  })
export class AuthenticationGuardService implements CanActivate{
    constructor(private authenticationService: AuthenticationService, private router: Router) { }
    
    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
            // console.log(this.authenticationService.isUserLoggedIn());
        if (this.authenticationService.isUserLoggedIn()) { // determine if the user is logged in from this method.
            return true;
        }
        this.router.navigate(['/login']);
        return false;
    }
}