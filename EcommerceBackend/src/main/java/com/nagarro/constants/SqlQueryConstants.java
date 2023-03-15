package com.nagarro.constants;

public class SqlQueryConstants {
	
//	public static final String GENERATE_SALES_REPORT = "Select date_created , product_name, name, email, order_id, quantity, price , discount"
//			+ "  from orders"
//			+ "  join order_product"
//			+ "  on orders.id = order_product.order_id"
//			+ "  join products"
//			+ "  on order_product.product_id = products.product_id"
//			+ "  join users"
//			+ "  on order_product.user_id = users.user_id "
//			+ "  where (orders.date_created >= :fromDate and orders.date_created <= :toDate)"
//			+ "  and brand = :brand and category = :category and add_by_user_id = :userId";
	public static final String GENERATE_SALES_REPORT = "select * from sales_report where (date_created >= :fromDate and date_created <= :toDate)"
			+ "and brand = :brand and category = :category and add_by_user_id = :userId";
	
	public static final String FIND_BRANDS = "SELECT brand FROM products";
	public static final String FIND_CATEGORIES = "SELECT category FROM products";
	public static final String GENERATE_AUTOCOMPLETE_LIST = "Select brand,category,product_name from products";

}
