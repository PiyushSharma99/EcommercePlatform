import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FailModalComponent } from '../fail-modal/fail-modal.component';
import { SalesReportGenerated, SalesReportSearch } from '../model/sales-report-model';
import { AuthenticationService } from '../service/authentication-service';

@Component({
  selector: 'app-vendor-report',
  templateUrl: './vendor-report.component.html',
  styleUrls: ['./vendor-report.component.css'],
})
export class VendorReportComponent implements OnInit{

  salesReportModel = new SalesReportSearch;
  brands : [] = [];
  categories : [] = [];
  name : any;
  retrievedImage:any; 
  salesReport : SalesReportGenerated[];
  netTotal:number = 0;
  showNetToal: string = '';
  noDataFound: boolean = false;

   constructor(private httpClient : HttpClient,
    private router : Router,
    private authenticationService: AuthenticationService,
    private modalService: NgbModal,
  ) { }

  ngOnInit(): void {
    if(this.authenticationService.isUserLoggedIn()) {
      if(this.authenticationService.getRole() === 'CUSTOMER') {
        this.authenticationService.logout();
        this.router.navigate(['login']);
      }
    }
    this.salesReport = [];
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
   onSubmit(reportForm : any) {
    const salesReportModelNotEmpty = Object.values(this.salesReportModel).includes('');
    const salesReportModeNotNull = Object.values(this.salesReportModel).includes(null);
    console.log(this.salesReportModel);
    if(!salesReportModelNotEmpty && !salesReportModeNotNull && !(this.salesReportModel.category === undefined) && !(this.salesReportModel.brand === undefined )) {

      this.httpClient.post<any>('http://localhost:8080/ecommerce/salesreport/'+this.authenticationService.getLoggedInUserName(),this.salesReportModel).subscribe(
          response => {
              if(response.status === 200 && response.message === 'Sales Report Generated') {
                // this.salesReport = response.data;
                for(var val of response.data) {
                  val.effectivePrice = (val.price - val.discount);
                  val.total = val.effectivePrice * val.quantity;
                  this.netTotal+= val.total;
                  this.salesReport.push(
                    new SalesReportGenerated(val.dateCreated,val.productName,val.name,val.email,val.orderId,val.quantity,
                     Number(val.price).toFixed(2).toString(), Number(val.discount).toFixed(2).toString(),
                     Number(val.effectivePrice).toFixed(2).toString(), Number(val.total).toFixed(2).toString()));
                  
                }
                this.showNetToal = Number(this.netTotal).toFixed(2).toString();
                this.noDataFound = false;
              }
              if(response.status === 200 && response.message === 'No details found for given request') {
                  this.noDataFound = true;
                  this.salesReport = [];
                  
              }
              
          }, 
          error => {
              const modalRef = this.modalService.open(FailModalComponent);
              modalRef.componentInstance.failMessage = 'Some error occured while generating your report. Try Again!!';
              modalRef.componentInstance.buttonMessage = 'Generate Other Reports';
          }
        );
    }

   }

   onReset(reportForm : any) {
      this.salesReport = [];
      this.noDataFound = false;
      reportForm.resetForm();
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
  onLogout() {
    this.authenticationService.logout();
    this.router.navigate(['login']);
  }
}
