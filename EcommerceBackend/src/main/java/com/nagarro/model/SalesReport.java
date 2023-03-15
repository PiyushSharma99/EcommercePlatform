package com.nagarro.model;

import java.time.LocalDate;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SalesReport")
public class SalesReport {
	
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column
	private LocalDate dateCreated;

	@Column
	private String productName;
	
	
	public SalesReport( LocalDate dateCreated, String productName, String email, String name, Long orderId,
			Integer quantity, double discount, double price,String brand,String category,Long addByUserId) {
		super();
		
		this.dateCreated = dateCreated;
		this.productName = productName;
		this.email = email;
		this.name = name;
		this.orderId = orderId;
		this.quantity = quantity;
		this.discount = discount;
		this.price = price;
		this.brand=brand;
		this.category=category;
		this.addByUserId = addByUserId;
	}

	@Column
	private String email;
	@Column
	private String name;
	@Column
	private Long orderId;
	
	@Column
	private Integer quantity;
	
	@Column
	private double discount;
	
	@Column
	private double price;
	
	@Column
	private String brand;
	
	@Column
	private String category;

	@Column
	private Long addByUserId;

}
