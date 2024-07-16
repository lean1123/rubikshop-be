package com.example.rubikShopApi.controller.user;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rubikShopApi.config.VNPConfig;
import com.example.rubikShopApi.entity.Order;
import com.example.rubikShopApi.entity.OrderDetail;
import com.example.rubikShopApi.entity.OrderDetailPrimaryKey;
import com.example.rubikShopApi.entity.ShippingInfo;
import com.example.rubikShopApi.entity.User;
import com.example.rubikShopApi.request.CartItem;
import com.example.rubikShopApi.request.OrderRequest;
import com.example.rubikShopApi.service.IOrderDetailService;
import com.example.rubikShopApi.service.IOrderService;
import com.example.rubikShopApi.service.IUserService;
import com.example.rubikShopApi.service.IVNPService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/order")
public class OrderController {
	
	@Autowired
	IOrderService orderService;
	
	@Autowired
	IOrderDetailService orderDetailService;
	
	@Autowired
	IUserService userService;
	
	@Autowired
	IVNPService ivnpService;
	
	@PostMapping("/create")
	public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderData, HttpServletRequest request) throws UnsupportedEncodingException {
	    // Find the user by ID
	    Optional<User> user = userService.findById(orderData.getUserID());
	    
	    
	    
	    
	    if (user.isPresent()) {
	        Order entity = new Order();
	        
	        BeanUtils.copyProperties(orderData, entity);
	        
	        
	        
	        entity.setUser(user.get());
	        
	        ShippingInfo shipInfo = new ShippingInfo("", orderData.getRoadName(), orderData.getDistrict(), orderData.getCity());
	        entity.setShippingInfo(shipInfo);
	        
	        
	        entity.setOrderDate(LocalDate.now());
	        
	        if(entity.getPaymentMethod().equalsIgnoreCase("VNPAY")) {
	        	String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
	            String vnpayUrl = ivnpService.createOrder(request, 1000000, "abc", VNPConfig.vnp_ReturnUrl);
	            System.out.println(vnpayUrl);
	            return ResponseEntity.status(HttpStatus.OK).body("url:" + vnpayUrl);
	        }
	        
	        orderService.save(entity);
	        
	        List<CartItem> cartItems = orderData.getCartItems();
	       
	        for (CartItem cartItem : cartItems) {
	            OrderDetail orderDetail = new OrderDetail();
	            
	            System.out.println(cartItem.getProduct());
	            
	            
	            orderDetail.setProduct(cartItem.getProduct());
	            orderDetail.setOrder(entity);
	            orderDetail.setQuantity(cartItem.getQuantity());
	            
	            OrderDetailPrimaryKey primaryKey = new OrderDetailPrimaryKey(entity.getOrderID(), cartItem.getProduct().getProductID());
	            orderDetail.setId(primaryKey);
	            
	            orderDetailService.save(orderDetail);
	        }
	        
	        return ResponseEntity.ok().body(entity);
	    }
	    
	    return ResponseEntity.badRequest().body(null);
	}


}
