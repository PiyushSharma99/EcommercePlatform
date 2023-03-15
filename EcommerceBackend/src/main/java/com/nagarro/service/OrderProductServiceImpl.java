package com.nagarro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nagarro.model.OrderProduct;
import com.nagarro.repository.OrderProductRepository;

@Service
public class OrderProductServiceImpl implements OrderProductService{

	@Autowired
	OrderProductRepository orderProductRepository;
	
	@Override
	public OrderProduct createOrderProduct(OrderProduct orderPorduct) {
		return orderProductRepository.save(orderPorduct);
	}

}
