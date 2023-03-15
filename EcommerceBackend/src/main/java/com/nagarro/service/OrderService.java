package com.nagarro.service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.repository.query.Param;

import com.nagarro.model.Order;

public interface OrderService {
	Iterable<Order> getAllOrders();  
	Order save(Order order);
	Optional<Order> getOrder(Long orderId);
//	Set<Object[]> generateSalesReport(LocalDate fromDate, LocalDate toDate, String brand ,
//			String category, Long userId);
}
