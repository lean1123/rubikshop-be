/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.rubikShopApi.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "orderId", unique = true, nullable = false)
	private int orderID;
	@ManyToOne
	@JoinColumn(name = "userId")
	private User user;
	private String paymentMethod;
	private LocalDate orderDate;
	private Double totalAmount;

	@ToString.Exclude
	@JsonIgnore
	@OneToMany(mappedBy = "order", cascade = { CascadeType.ALL })
	private List<OrderDetail> orderDetail;

	private boolean isPaymented;

	@Embedded
	private ShippingInfo shippingInfo;
	
	@OneToOne(mappedBy = "order")
	private PaymentInfo paymentInfo;

	public Order(User user, LocalDate orderDate, List<OrderDetail> orderDetail, String paymentMethod) {
		this.user = user;
		this.orderDate = orderDate;
		this.orderDetail = orderDetail;
		this.paymentMethod = paymentMethod;
	}


}
