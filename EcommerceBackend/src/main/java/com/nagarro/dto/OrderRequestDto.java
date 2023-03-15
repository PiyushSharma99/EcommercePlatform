package com.nagarro.dto;

import com.nagarro.model.Product;
import com.nagarro.request.ProductRequest;

import lombok.Data;

@Data
public class OrderRequestDto {
	private Product product;
    private Integer quantity;
}
