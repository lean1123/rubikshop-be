package com.example.rubikShopApi.service;

import java.io.UnsupportedEncodingException;

import jakarta.servlet.http.HttpServletRequest;


public interface IVNPService {

	int orderReturn(HttpServletRequest request);

	String createOrder(HttpServletRequest request, double amount, String orderInfor, String urlReturn) throws UnsupportedEncodingException;

}
