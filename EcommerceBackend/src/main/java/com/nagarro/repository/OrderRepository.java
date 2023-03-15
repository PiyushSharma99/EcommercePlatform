package com.nagarro.repository;

import java.time.LocalDate;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nagarro.constants.SqlQueryConstants;
//import com.nagarro.dto.SalesReportDto;
import com.nagarro.model.Order;
import com.nagarro.model.Product;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	Optional<Order> findById(Long orderId);

	List <Order> findAll();
	
//	@Query(nativeQuery = true, value = SqlQueryConstants.GENERATE_SALES_REPORT)
//	Set<Object[]> generateSalesReport(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate
//			,@Param("brand") String brand , @Param("category") String category, @Param("userId") Long userId); // generate sales report based on date brand and category
}
