package com.example.rubikShopApi.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ShippingInfo {
	private String additionInfo;
	private String roadName;
	private String district;
	private String city;
}
