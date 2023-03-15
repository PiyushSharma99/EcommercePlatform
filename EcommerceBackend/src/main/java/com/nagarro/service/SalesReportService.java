package com.nagarro.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.nagarro.model.Role;
import com.nagarro.model.SalesReport;

public interface SalesReportService {
//	Optional<List> findByName(String role);
//	List <SalesReport> findSalesReport(LocalDate dateCreated);

	List<SalesReport> findSalesReport(LocalDate fromDate, LocalDate toDate, String brand, String category, Long userId);

	SalesReport save(SalesReport salesReport);

}
