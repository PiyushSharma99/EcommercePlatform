import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FailModalComponent } from '../fail-modal/fail-modal.component';
import { ProductOrder } from '../model/order-model';
import { AuthenticationService } from '../service/authentication-service';
import { VendorEditComponent } from '../vendor-edit/vendor-edit.component';

@Component({
  selector: 'app-vendor-view',
  templateUrl: './vendor-view.component.html',
  styleUrls: ['./vendor-view.component.css']
})
export class VendorViewComponent implements OnInit{
  retrievedImage : any;
  name: any;
  username:any;
  productOrders: ProductOrder[] = [];
  brands:any [];
  category:any[];

  constructor (
    private httpClient : HttpClient,
    private router : Router,
    private modalService: NgbModal,
    private authenticationService: AuthenticationService
    
  ) {}
  
  ngOnInit(): void {
    if(this.authenticationService.isUserLoggedIn()) {
      if(this.authenticationService.getRole() === 'CUSTOMER') {
        this.authenticationService.logout();
        this.router.navigate(['login']);
      }
    }
    document.getElementById('footer').style.visibility = "hidden";
    this.productOrders=[];
    this.brands = [];
    this.category = [];
    this.getProducts();
    this.getUser();
    
  }

  getProducts() {
    this.httpClient.get<any>('http://localhost:8080/ecommerce/products/'+this.authenticationService.getLoggedInUserName()).subscribe(
      response => {
        for (var val of response.data) {
            val.productImage = 'data:image/jpeg;base64,' + val.productImage;
            let effectivePrice = (val.price - val.discount) ;
            let isDiscountZero = false;
            if(val.discount === 0) {
              isDiscountZero = true;
            }
            this.productOrders.push(new ProductOrder(val, 0,false,JSON.parse(JSON.stringify(Number(effectivePrice).toFixed(2))),isDiscountZero));
       this.brands.push(val.brand);
       this.category.push(val.category);
          }
        },
        error => {
          if(error) {
            const modalRef = this.modalService.open(FailModalComponent);
                modalRef.componentInstance.failMessage = 'Some error occured while loading the product. Try Again!!';
                modalRef.componentInstance.buttonMessage = 'Add More Products';
          }
        });
  }
 
  getUser() {
      this.httpClient.get<any>('http://localhost:8080/ecommerce/user/'+this.authenticationService.getLoggedInUserName()).subscribe(
      response => {
          this.name = response.data.name;
          this.retrievedImage = 'data:image/jpeg;base64,' + response.data.userImage;
        }
      )
  }
  

  onAdd(){
    this.router.navigate(['vendor/add']);
  }
  onEdit(order:ProductOrder) {
    const modalRef = this.modalService.open(VendorEditComponent, {size: 'lg', windowClass: 'modal-xl'});
    modalRef.componentInstance.editOrder = order.product;
    modalRef.componentInstance.event.subscribe((emmitedValue: any) => {
      if(emmitedValue.orderEdited) {
        this.ngOnInit();
      }
    })
  }
  onSales() {
    this.router.navigate(['vendor/sales']);
  }
  onLogout() {
    this.authenticationService.logout();
    this.router.navigate(['login']);
  }

}
