package com.example.rubikShopApi.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResponseObject {
	
	private String statusCode;
	private String message;
	private Object data;
	
}
