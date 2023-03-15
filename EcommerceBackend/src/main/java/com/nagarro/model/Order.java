package com.nagarro.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.nagarro.request.ProductRequest;

import lombok.Data;

@Data
@Entity
@Table(name = "orders")
public class Order {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	  
	@JsonFormat(pattern = "dd/MM/yyyy")
	@Column
	private LocalDate dateCreated;
	
	@Column(name = "total_price")
    private Double totalPrice;
	
	@OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "orderId",insertable = false,updatable = false)
    private List<OrderProduct> orderProducts = new ArrayList<>();
	
}
