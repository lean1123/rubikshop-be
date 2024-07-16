package com.example.rubikShopApi.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginForm {
	private String email;
	private String password;
}
