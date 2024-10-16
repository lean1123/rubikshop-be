package com.example.rubikShopApi.request;

import com.google.common.base.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResponseObject {
	private String token;
	private String statusCode;
	private String message;
	private Object data;
	
}
