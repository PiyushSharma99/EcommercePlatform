export class ProductFormModel {
    public productName: string = '';
    public productDescription: string = '';
    public productCode: string = '';
    public category: string = '';
    public price: string = '';
    public brand: string;
    public discount: string = '';
    public productImage!: any;
}

export class AddProductModel {
    public productName: string = '';
    public productDescription: string = '';
    public productCode: string = '';
    public category: string = '';
    public price: string = '';
    public brand: string;
    public discount: string = '';
    public productImage!: FormData;
    constructor(productName:string,productDescription:string,productCode:string,category:string, brand:string, price:string, productImage:FormData,discount:string) { 
        this.productName = productName 
        this.productDescription = productDescription
        this.productCode = productCode
        this.category = category
        this.price = price
        this.discount = discount
        this.brand = brand
        this.productImage = productImage
     }  
}