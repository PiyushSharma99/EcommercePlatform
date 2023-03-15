package com.nagarro.request;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

import com.nagarro.model.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ProductRequest {
	
	private String productName;

	private String productDescription;

	private String productCode;
	
	private String category;
	
	private String price;
	
	private String discount;

	private String brand;
	
}
