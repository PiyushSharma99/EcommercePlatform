package com.nagarro.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nagarro.constants.SqlQueryConstants;
import com.nagarro.model.Role;
import com.nagarro.model.SalesReport;

public interface SalesReportRepository extends JpaRepository<SalesReport, Long>{
	
	@Query(nativeQuery = true, value = SqlQueryConstants.GENERATE_SALES_REPORT)
	List<SalesReport> generateSalesReport(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate,@Param("brand") String brand , @Param("category") String category, @Param("userId") Long userId);

}
