import { ProductFormModel } from "./vendor-add-models";

export class ProductOrder {
    product: ProductFormModel;
    quantity: number;
    currentProductSelected: boolean;
    effectivePrice: number;
    isDiscountZero: boolean;
    constructor(product: ProductFormModel, quantity: number, currentProductSelected: boolean, effectivePrice: number,isDiscountZero: boolean) {
        this.product = product;
        this.quantity = quantity;
        this.currentProductSelected = currentProductSelected;
        this.effectivePrice = effectivePrice;
        this.isDiscountZero = isDiscountZero;
    }

}

export class BuyOrder{
    orders:ProductOrder[];
    username:string='';
}