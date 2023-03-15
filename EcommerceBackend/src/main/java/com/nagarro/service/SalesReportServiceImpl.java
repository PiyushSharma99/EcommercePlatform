package com.nagarro.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nagarro.model.SalesReport;
import com.nagarro.repository.SalesReportRepository;

@Service
public class SalesReportServiceImpl implements SalesReportService{

	@Autowired
	SalesReportRepository salesReportRepository;
	
	@Override
	public List<SalesReport> findSalesReport(LocalDate fromDate, LocalDate toDate, String brand, String category,
			Long userId) {
		// TODO Auto-generated method stub
		
		
//		List salesReportRepository
		return salesReportRepository.generateSalesReport(fromDate, toDate, brand, category, userId);
	}

	@Override
	public  SalesReport save(SalesReport salesReport) {
		// TODO Auto-generated method stub
		return salesReportRepository.save(salesReport);
		
	}

}
