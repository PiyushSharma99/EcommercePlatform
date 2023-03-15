export class SalesReportSearch{
    fromDate : string ='';
    toDate : string ='';
    brand!: string;
    category!: string;
}

export class SalesReportGenerated{
    dateCreated: string ='';
	productName: string ='';
	name: string ='';
	email: string ='';
	orderId: string ='';
	quantity!: number;
	price!: string;
	discount: string;
    effectivePrice!: string;
    total!:string;
    constructor(dateCreated:string,productName:string,name:string,email:string, orderId:string,quantity:number,
        price:string,discount:string, effectivePrice:string,total:string) { 
        this.name = name 
        this.email = email
        this.dateCreated = dateCreated
        this.productName = productName
        this.orderId = orderId
        this.price = price
        this.quantity = quantity
        this.discount = discount
        this.effectivePrice = effectivePrice
        this.total = total
     } 
}