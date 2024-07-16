package com.example.rubikShopApi.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
	
	/* 
	 const order = {
      userID: user.userID,
      paymentMethod: values.paymentMethod,
      fullName: values.fullName,
      email: values.email,
      address: values.address,
      roadName: values.roadName,
      district: values.district,
      city: values.city,
      cartItems,
    };
	 */
	
	
	private int userID;
	private String paymentMethod;
	private String fullName;
	private String email;
	private String address;
	private String roadName;
	private String district;
	private String city;
	private List<CartItem> cartItems;
	private Double totalAmount;
}
