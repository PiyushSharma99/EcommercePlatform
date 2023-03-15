import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { AddProductModel, ProductFormModel } from '../model/vendor-add-models';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { SuccessModalComponent } from '../success-modal/success-modal.component';
import { FailModalComponent } from '../fail-modal/fail-modal.component';
import { AuthenticationService } from '../service/authentication-service';
@Component({
  selector: 'app-vendor-add',
  templateUrl: './vendor-add.component.html',
  styleUrls: ['./vendor-add.component.css']
})
export class VendorAddComponent implements OnInit{
  retrievedImage : any;
  name: any;
  username:any;
  productModel = new ProductFormModel;
  addProductFormData = new FormData();
  brands: any[] = []; // to save brands from db
  categories: any[] = []; // to save categories from db
  productCodeIsAlreadyPresent : boolean = false;
  discountGreaterThanPrice : boolean = false;

  constructor (
    private httpClient : HttpClient,
    private authenticationService : AuthenticationService,
    private router : Router,
    private modalService: NgbModal
  ) {}
  
  ngOnInit(): void {
    if(this.authenticationService.isUserLoggedIn()) {
      if(this.authenticationService.getRole() === 'CUSTOMER') {
        this.authenticationService.logout();
        this.router.navigate(['login']);
      }
    }
    this.getUser();
    this.getBrands();
    this.getCategories();
  }

  getUser() {
      this.httpClient.get<any>('http://localhost:8080/ecommerce/user/'+this.authenticationService.getLoggedInUserName()).
      subscribe(
      response => {
          this.name = response.data.name;
          this.retrievedImage = 'data:image/jpeg;base64,' + response.data.userImage;
        }
      )
  }
  getBrands() {
    this.httpClient.get<any>('http://localhost:8080/ecommerce/product/brands').subscribe(
      response => {
          this.brands = response.data;
        }
      )
  }

  getCategories() {
    this.httpClient.get<any>('http://localhost:8080/ecommerce/product/categories').subscribe(
      response => {
          this.categories = response.data;
        }
      )
  }
  onSubmit(registerForm : NgForm){
    // check if register model is not empty and null
    const productModelNotEmpty = Object.values(this.productModel).includes('');
    const productModeNotNull = Object.values(this.productModel).includes(null);
    

    if(!productModelNotEmpty && !productModeNotNull) { 
      const addProductModel = new AddProductModel(this.productModel.productName, this.productModel.productDescription,
        this.productModel.productCode, this.productModel.category, this.productModel.brand, 
        this.productModel.price, this.addProductFormData,this.productModel.discount);
        this.addProductFormData.delete('productData');
        this.addProductFormData.append('productData',new Blob([JSON.stringify(addProductModel)], {type: 'application/json'}));
        this.httpClient.post<any>('http://localhost:8080/ecommerce/product/add/'+this.authenticationService.getLoggedInUserName(),this.addProductFormData).subscribe(
          response => {

            if(response.status === 201) {
              this.onReset(registerForm);
              this.productCodeIsAlreadyPresent = false;
              this.discountGreaterThanPrice = false;
              const modalRef = this.modalService.open(SuccessModalComponent);
              modalRef.componentInstance.successMessage = 'Product has been added Successfully.';
              modalRef.componentInstance.buttonMessage = 'Add More Products';
            } 

            else if(response.status === 200 ) { // to show already present product code error
              if(response.message === 'Product With Given Code Is Already Present') {
                this.productCodeIsAlreadyPresent = true;
              }
              else if (response.message === 'Discount Must Be Less Than Price') {
                this.discountGreaterThanPrice = true;
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
    this.productCodeIsAlreadyPresent = false;
    this.discountGreaterThanPrice = false;
    registerForm.resetForm();
    this.addProductFormData = new FormData();
  }

  // when image upload uploads this will execute
  onImageUpload(imageInput: any) {
    this.addProductFormData.append('imageFile', imageInput.files[0], imageInput.files[0].name);
  }
  
  onLogout() {
    this.authenticationService.logout();
    this.router.navigate(['login']);
  }
}
