package com.nagarro.model;

import java.util.ArrayList;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"productCode"} ) } )
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long productId;
	
	@Column
	private String productName;

	@Column
	private String productDescription;

	@Column
	private String productCode;
	
	@Column
	private String category;
	
	@Column
	private double price;
	
	@Column
	private double discount;
	
	@Column
	private String brand;
	
	@Lob
	@Column(columnDefinition = "mediumblob")
	private byte[] productImage;	
	
	@Column
	private Long addByUserId;
}
