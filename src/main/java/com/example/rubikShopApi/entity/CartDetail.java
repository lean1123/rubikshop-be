/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.rubikShopApi.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class CartDetail {
	@Id
	@OneToOne
	@JoinColumn(name = "productId")
	private Product product;
	
	@Id
	@ManyToOne
	@JoinColumn(name = "cartId")
	private Cart cart;
    private int quantity;

    public CartDetail(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }
}
