package com.example.rubikShopApi.request;

import com.example.rubikShopApi.entity.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterForm {
	
	@NotEmpty
	private String fullName;
	private String email;
	private String password;
	private String retypePassword;
	private String address;
	private Role role = Role.USER;
	private String imageUrl;
	
	private Boolean isOauth2 = false;
	
	
	
}
