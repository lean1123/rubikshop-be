package com.example.rubikShopApi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.annotation.Generated;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Table(name = "Payments")
public class PaymentInfo {
	
	@Id
	@Column(name = "paymentInfoID", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int paymentInfoID;
	
	@JsonIgnore
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "orderID")
	private Order order;
	
	private String payContent;
	
	private String paymentTime;
	
	private String transactionId;
}
