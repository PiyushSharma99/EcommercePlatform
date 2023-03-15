package com.nagarro.service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nagarro.model.Order;
import com.nagarro.repository.OrderRepository;

@Service
public class OrderServiceImpl implements OrderService{

	@Autowired
	OrderRepository orderRepository;
	
	@Override
	public Iterable<Order> getAllOrders() {
		return orderRepository.findAll();
	}

	@Override
	public Order save(Order order) {
        return orderRepository.save(order);
	}

	@Override
	public Optional<Order> getOrder(Long orderId) {
		return orderRepository.findById(orderId);
	}

//	@Override
//	public Set<Object[]> generateSalesReport(LocalDate fromDate, LocalDate toDate, String brand, String category,
//			Long userId) {
//		return orderRepository.generateSalesReport(fromDate, toDate, brand, category, userId);
//	}

}
