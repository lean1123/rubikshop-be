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
	@NotEmpty(message = "Email is required")
    @Email(message = "Invalid email format")
	private String email;
	@NotEmpty(message = "Password is required")
	@Min(value = 6)
	private String password;
	@NotEmpty(message = "Retype password is required")
	@Min(value = 6)
	private String retypePassword;
	@NotEmpty(message = "Address is required")
	private String address;
	private Role role = Role.USER;
	
	
	
}
