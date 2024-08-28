/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.rubikShopApi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "order_details")
public class OrderDetail {

	@EmbeddedId
	private OrderDetailPrimaryKey id = new OrderDetailPrimaryKey();
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "order_Id")
	@MapsId("orderId")
	private Order order;

	
	@ManyToOne
	@JoinColumn(name = "product_Id")
	@MapsId("productId")
	private Product product;
	private int quantity;

	public OrderDetail(Product product, int quantity) {
		this.product = product;
		this.quantity = quantity;
	}
}
