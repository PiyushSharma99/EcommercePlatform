import { HttpClient } from '@angular/common/http';
import { AfterViewInit, Component, EventEmitter, OnInit, ViewChild } from '@angular/core';
import { Data, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { ProductOrder } from '../model/order-model';
import { ProductFormModel } from '../model/vendor-add-models';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { BuyNowViewComponent } from '../buy-now-view/buy-now-view.component';
import { AuthenticationService } from '../service/authentication-service';
import { FormControl } from '@angular/forms';
import { FailModalComponent } from '../fail-modal/fail-modal.component';
@Component({
  selector: 'app-customer-view',
  templateUrl: './customer-view.component.html',
  styleUrls: ['./customer-view.component.css'],
})
export class CustomerViewComponent implements OnInit {
  productOrders: ProductOrder[] = [];
  buyNowOrder: ProductOrder[] = [];
  cartOrders: ProductOrder[] = [];
  products: ProductFormModel[] = [];
  searchText: any;

  subscription: Subscription;

  userImage: any;
  name: any;
  username: any;
  productsAddedToCart: ProductFormModel[];

  number: any;
  public event: EventEmitter<any> = new EventEmitter();
  myControl = new FormControl();
  constructor(
    private httpClient: HttpClient,
    private modalService: NgbModal,
    private authenticationService: AuthenticationService,
    private router: Router,
    // private search: ComboBoxComponent
  ) {}

  ngOnInit(): void {
    

    if (this.authenticationService.isUserLoggedIn()) {
      if (this.authenticationService.getRole() === 'VENDOR') {
        this.authenticationService.logout();
        this.router.navigate(['login']);
      }
    }
    this.cartOrders = [];
    this.productOrders = [];
    this.products = [];
    this.getProducts();
    this.getUser();
  }

  getUser() {
    this.httpClient
      .get<any>(
        'http://localhost:8080/ecommerce/user/' +
          this.authenticationService.getLoggedInUserName()
      )
      .subscribe((response) => {
        this.name = response.data.name;
        this.userImage = 'data:image/jpeg;base64,' + response.data.userImage;
      });
  }
  getProducts() {
    this.httpClient
      .get<any>('http://localhost:8080/ecommerce/products')
      .subscribe((response) => {
        for (var val of response.data) {
          val.productImage = 'data:image/jpeg;base64,' + val.productImage;
          const effectivePrice = (val.price) - (val.discount);
          let isDiscountZero = false;
          if (val.discount === 0) {
            isDiscountZero = true;
          }
          this.productOrders.push(
            new ProductOrder(val, 0, false,JSON.parse(JSON.stringify(Number(effectivePrice).toFixed(2))), isDiscountZero)
          );
        }
       
        this.productOrders;
      },
      error => {
        if(error) {
          const modalRef = this.modalService.open(FailModalComponent);
              modalRef.componentInstance.failMessage = 'Some error occured while loading the product. Try Again!!';
              modalRef.componentInstance.buttonMessage = 'Back To Shopping';
        }
      });
  }
  
  checkIfExist(productOrder: ProductFormModel) {
    const indexCartOrder = this.getProductIndexForCart(productOrder);
    if (indexCartOrder > -1) {
      this.cartOrders.splice(indexCartOrder, 1);
    }
  }

  // getProductIndex for Customer View Page
  getProductIndex(product: ProductFormModel): number {
    return this.productOrders.findIndex((value) => value.product === product);
  }

  //getProductIndex for Cart Products Page
  getProductIndexForCart(product: ProductFormModel): number {
    return this.cartOrders.findIndex((value) => value.product === product);
  }

  buyNow(order: ProductOrder) {
    this.buyNowOrder = [];
    this.buyNowOrder.push(order);
    const total = this.buyNowOrder.at(0).effectivePrice;
    const modalRef = this.modalService.open(BuyNowViewComponent);
    modalRef.componentInstance.orders = this.buyNowOrder;
    modalRef.componentInstance.total = Number(total).toFixed(2);
  }

  onLogout() {
    this.authenticationService.logout();
    this.router.navigate(['login']);
  }
}
