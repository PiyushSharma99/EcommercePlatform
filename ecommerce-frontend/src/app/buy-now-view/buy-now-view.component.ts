import { HttpClient } from '@angular/common/http';
import { Component, Input } from '@angular/core';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FailModalComponent } from '../fail-modal/fail-modal.component';
import { BuyOrder, ProductOrder } from '../model/order-model';
import { AuthenticationService } from '../service/authentication-service';
import { SuccessModalComponent } from '../success-modal/success-modal.component';

@Component({
  selector: 'app-buy-now-view',
  templateUrl: './buy-now-view.component.html',
  styleUrls: ['./buy-now-view.component.css']
})
export class BuyNowViewComponent {
  @Input() orders:ProductOrder[] = [];
  @Input() total:number;
  
  buyOrder = new BuyOrder;
  buyOrders : ProductOrder[] = [];
  constructor(public activeModal: NgbActiveModal,
    private httpClient: HttpClient,
    private modalService: NgbModal,
    private authenticationService: AuthenticationService) {
    
   }

  finishOrder(orders: ProductOrder[], total : number){
    this.buyOrders = JSON.parse(JSON.stringify(orders)); // to copy the array without reference
    for(var val of this.buyOrders) {
      val.quantity = 1; // buy now order have always 1 quantity
      val.product.productImage = val.product.productImage.split('base64,')[1]
    }
    this.buyOrder.orders = this.buyOrders;
    this.buyOrder.username = this.authenticationService.getLoggedInUserName();
    
    this.httpClient.post<any>('http://localhost:8080/ecommerce/order',this.buyOrder).subscribe(
      response =>{
        if(response.status === 201) {
          this.activeModal.dismiss('Cross click');
          const modalRef = this.modalService.open(SuccessModalComponent);
          modalRef.componentInstance.buttonMessage = 'Back To Shopping';
          modalRef.componentInstance.successMessage = 'Your order has been created successfully.';
        }
      },
      error => {
        this.activeModal.dismiss('Cross click');
        const modalRef = this.modalService.open(FailModalComponent);
        modalRef.componentInstance.buttonMessage = 'Back To Shopping';
        modalRef.componentInstance.failMessage = 'Some error occured while adding your product. Try Again!!';
        console.log(error.message);
        return;
      }
    );
  }
}
