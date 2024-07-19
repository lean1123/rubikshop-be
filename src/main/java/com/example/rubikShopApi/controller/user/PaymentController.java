package com.example.rubikShopApi.controller.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.rubikShopApi.entity.Order;
import com.example.rubikShopApi.entity.PaymentInfo;
import com.example.rubikShopApi.service.IOrderService;
import com.example.rubikShopApi.service.IPaymentInfoService;

@RestController
@RequestMapping("/payments")
public class PaymentController {
	
	@Autowired
	IPaymentInfoService paymentInfoService;
	
	@Autowired
	IOrderService orderService;
	
	@GetMapping("order/{orderID}")
	public PaymentInfo getPaymentByOrderID(@PathVariable("orderID") int orderID) {
		Optional<Order> opt = orderService.findById(orderID);
		
		if(opt.isPresent() && opt.get().isPaymented()) {
			Optional<PaymentInfo> paymentOpt = paymentInfoService.findByOrder(opt.get());
			
			if(paymentOpt.get() != null) {
				return paymentOpt.get();
			}
		}
		
		return null;
	}
	
}
