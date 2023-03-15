import { HttpClient } from '@angular/common/http';
import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { NgForm } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { IDropdownSettings } from 'ng-multiselect-dropdown';
import { FailModalComponent } from '../fail-modal/fail-modal.component';
import { RegisterForm } from '../model/register-form-model';
import { User } from '../model/regsiter-user-model';
import { AuthenticationService } from '../service/authentication-service';
import { SuccessModalComponent } from '../success-modal/success-modal.component';

@Component({
  selector: 'app-signup-page',
  templateUrl: './signup-page.component.html',
  styleUrls: ['./signup-page.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class SignupPageComponent implements OnInit{

  registerModel = new RegisterForm;
  roleArray: any[] = []; // to submit  the role after form submit
  roles: any[] = []; // to fetch the available roles from db
  registerFormData = new FormData();
  retrievedImage: any;
  emailAlreadyPresent: boolean = false;
  usernameAlreadyPresent: boolean = false;
  dropdownSettings:IDropdownSettings={};

  constructor (
    private httpClient : HttpClient,
    private modalService: NgbModal,
    private authenticationService: AuthenticationService
  ) {}
  
  ngOnInit(): void {
    this.authenticationService.logout();
    this.emailAlreadyPresent = false;
    this.usernameAlreadyPresent = false;
    this.populateRolesFromDb();
    this.dropdownSettings = {
      singleSelection: false,
      idField: 'roleId',
      textField: 'name',
    };
    }

    populateRolesFromDb(): void {
      this.httpClient.get<any>('http://localhost:8080/ecommerce/roles').subscribe(
        response => {
          this.roles = response.data;
        }
      );
    }

  onSubmit(registerForm : NgForm) {
    this.emailAlreadyPresent = false;
    this.usernameAlreadyPresent = false;
    if(this.registerModel.role === undefined || this.registerModel.role === null) {
      return;
    }
    // check if register model is not empty and null
    const registerModelNotEmpty = Object.values(this.registerModel).includes('');
    const registerModeNotNull = Object.values(this.registerModel).includes(null);

    // populating roleArray after form submit
    for(let i=0; i < this.registerModel.role.length; i++) {
           this.roleArray.push(this.registerModel.role[i].name);
    }

    if(!registerModelNotEmpty && !registerModeNotNull) {
      
        const userModel = new User(this.registerModel.name, this.registerModel.email, this.registerModel.password,
        this.registerModel.username,this.registerFormData);

        userModel.setArray(this.roleArray); // to set the roles
        this.registerFormData.delete('userData');
        this.registerFormData.append('userData',new Blob([JSON.stringify(userModel)], {type: 'application/json'}));

        this.httpClient.post<any>('http://localhost:8080/ecommerce/auth/signup',this.registerFormData).subscribe(
          response => {
            if(response.status === 201) {
              this.onReset(registerForm);
              this.usernameAlreadyPresent = false;
              this.emailAlreadyPresent = false;
              const modalRef = this.modalService.open(SuccessModalComponent);
              modalRef.componentInstance.successMessage = 'User has been added Successfully.';
              modalRef.componentInstance.buttonMessage = 'Add More Users';
            } 
            else if(response.status === 200 ) {
              if(response.message === 'Username Is Already Present') {
                this.usernameAlreadyPresent = true;
              }
              else if(response.message === 'Email Is Already Present') {
                this.emailAlreadyPresent = true;
              }

            }
          },
          error => {
            const modalRef = this.modalService.open(FailModalComponent);
            modalRef.componentInstance.failMessage = 'Some error occured while adding your product. Try Again!!';
            modalRef.componentInstance.buttonMessage = 'Add More Products';
          }
        );
    }    
}

  onReset(registerForm : NgForm) {
    this.emailAlreadyPresent = false;
    this.usernameAlreadyPresent = false;
    registerForm.resetForm();
    this.roleArray = [];
    this.registerFormData = new FormData();
  }

  // when image upload uploads this will execute
  onImageUpload(imageInput: any) {
    this.registerFormData.append('imageFile', imageInput.files[0], imageInput.files[0].name);
  }
}
