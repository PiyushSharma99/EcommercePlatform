package com.nagarro.request;

import lombok.Data;

@Data
public class SalesReportRequest {
	private String fromDate;
	private String toDate;
	private String brand;
	private String category;
}
