package com.nagarro.model;

import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_product")
public class OrderProduct {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	
	@Column
	private Integer quantity;
	
	@Column(name = "productId")
    private Long productId;
	
    @Column(name = "orderId")
    private Long orderId;
	
    @Column(name = "userId") 
    private Long userId;
    
    @ManyToOne
    @JoinColumn(name = "orderId",insertable = false,updatable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "productId",insertable = false,updatable = false)
    private Product product;
	
    @ManyToOne
    @JoinColumn(name = "userId",insertable = false,updatable = false)
    private User user;
    
	public OrderProduct(Long orderId, Long productId, Integer quantity, Long userId) {
		this.productId = productId;
		this.orderId=orderId;
		this.quantity = quantity;
		this.userId = userId;
    }
}
