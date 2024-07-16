package com.example.rubikShopApi.entity;

import lombok.ToString;

@ToString
public enum Role {
	USER("User"),
	ADMIN("Admin");
	
	private String name;

	private Role(String name) {
		// TODO Auto-generated constructor stub
		this.name = name;
	}
}
