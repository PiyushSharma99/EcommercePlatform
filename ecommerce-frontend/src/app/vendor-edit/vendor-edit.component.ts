import { HttpClient } from '@angular/common/http';
import { Component, EventEmitter, Input, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FailModalComponent } from '../fail-modal/fail-modal.component';
import { ProductFormModel, AddProductModel } from '../model/vendor-add-models';
import { AuthenticationService } from '../service/authentication-service';
import { SuccessModalComponent } from '../success-modal/success-modal.component';
export class UpdateFormModel {
  public productName: string = '';
  public productDescription: string = '';
  public productCode: string = '';
  public category: string = '';
  public price: string = '';
  public brand: string;
  public discount: string = '';
  public productImage!: File;
}

@Component({
  selector: 'app-vendor-edit',
  templateUrl: './vendor-edit.component.html',
  styleUrls: ['./vendor-edit.component.css']
})
export class VendorEditComponent implements OnInit{

  @Input() editOrder = new ProductFormModel;
  constructor (
    private httpClient : HttpClient,
    private modalService: NgbModal,
    public activeModal: NgbActiveModal,
    private router : Router,
    private authenticationService: AuthenticationService
      ) {}

      oldImageData: any;
      uploadNewImage : string = 'yes';
      public event: EventEmitter<any> = new EventEmitter();
      retrievedImage : any;
      name: any;
      username:any;
      editProductModel = new ProductFormModel;
      addProductFormData = new FormData();
      brands: any[] = []; // to save brands from db
      categories: any[] = []; // to save categories from db
      productCodeIsAlreadyPresent : boolean = false;
      discountGreaterThanPrice : boolean = false;

  ngOnInit(): void {
    if(this.authenticationService.isUserLoggedIn()) {
      if(this.authenticationService.getRole() === 'CUSTOMER') {
        this.authenticationService.logout();
        this.router.navigate(['login']);
      }
    }
    this.editProductModel = JSON.parse(JSON.stringify(this.editOrder));
    this.oldImageData = JSON.parse(JSON.stringify(this.editOrder.productImage));
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
    
    if(this.uploadNewImage === 'yes') {
      if(this.editProductModel.productImage.split(',')[0] === 'data:image/jpeg;base64') {
        this.editProductModel.productImage = null;
      }
    }

    if(this.uploadNewImage === 'no' ) {
      this.editProductModel.productImage = this.oldImageData; // if not want to upload image covert it into file with blob object
      var byteString = atob(this.oldImageData.split(',')[1]);
      var ab = new ArrayBuffer(byteString.length);
      var ia = new Uint8Array(ab);
    
      for (var i = 0; i < byteString.length; i++) {
        ia[i] = byteString.charCodeAt(i);
      }
      var files =  new Blob([ab], { type: 'image/jpeg' });
      // this.editProductModel.productImage = this.editProductModel.productImage.split(',')[1];
      var file = new File([files], "images");
      this.addProductFormData.append('imageFile', file, file.name);

      }

    // check if register model is not empty and null
    const editProductModelNotEmpty = Object.values(this.editProductModel).includes('');
    const productModeNotNull = Object.values(this.editProductModel).includes(null);

    if(!editProductModelNotEmpty && !productModeNotNull) { 
      const editProductModel = new AddProductModel(this.editProductModel.productName, this.editProductModel.productDescription,
        this.editProductModel.productCode, this.editProductModel.category, this.editProductModel.brand, 
        this.editProductModel.price, this.addProductFormData,this.editProductModel.discount);
        this.addProductFormData.delete('productData');
        this.addProductFormData.append('productData',new Blob([JSON.stringify(editProductModel)], {type: 'application/json'}));
        this.httpClient.post<any>('http://localhost:8080/ecommerce/product/edit',this.addProductFormData).subscribe(
          response => {

            if(response.status === 200 && response.message === 'Product Edited Successfully') {
              this.onReset(registerForm);
              this.discountGreaterThanPrice = false;
              this.activeModal.dismiss('Cross click');
              const modalRef = this.modalService.open(SuccessModalComponent);
              modalRef.componentInstance.successMessage = 'Product has been edited Successfully.';
              modalRef.componentInstance.buttonMessage = 'Edit More Products';
              this.event.emit({orderEdited: true});
            } 

            else if(response.status === 200 ) { // to show already present product code error
              if (response.message === 'Discount Must Be Less Than Price') {
                this.discountGreaterThanPrice = true;
              }
            }
          }, 
          error => {
              this.activeModal.dismiss('Cross click');
              const modalRef = this.modalService.open(FailModalComponent);
              modalRef.componentInstance.failMessage = 'Some error occured while editing your product. Try Again!!';
              modalRef.componentInstance.buttonMessage = 'Edit More Products';
          }
        );
    }
    
    

  }
  // when image upload this will execute
  onImageUpload(imageInput: any) {
    this.addProductFormData.delete('imageFile');
    this.addProductFormData.append('imageFile', imageInput.files[0], imageInput.files[0].name);
  }

  onReset(registerForm : NgForm) {
    this.discountGreaterThanPrice = false;
    registerForm.resetForm();
    this.addProductFormData = new FormData();
  }
}
