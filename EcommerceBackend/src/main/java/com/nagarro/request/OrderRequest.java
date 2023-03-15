package com.nagarro.request;

import java.util.List;

import com.nagarro.dto.OrderRequestDto;

import lombok.Data;

@Data
public class OrderRequest {
	 private List<OrderRequestDto> orders;
	 private String username;
}