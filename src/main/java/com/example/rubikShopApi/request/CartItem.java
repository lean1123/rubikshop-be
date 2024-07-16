package com.example.rubikShopApi.request;

import com.example.rubikShopApi.entity.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
	private int id;
	private Product product;
	private int quantity;
}
