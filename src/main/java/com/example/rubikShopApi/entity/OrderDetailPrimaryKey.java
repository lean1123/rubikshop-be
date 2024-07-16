package com.example.rubikShopApi.entity;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class OrderDetailPrimaryKey implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int orderId;
    private int productId;

    // Constructor
    public OrderDetailPrimaryKey(int orderId, int productId) {
        this.orderId = orderId;
        this.productId = productId;
    }
}
