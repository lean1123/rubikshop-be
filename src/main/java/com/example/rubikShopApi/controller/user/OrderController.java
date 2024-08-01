package com.example.rubikShopApi.controller.user;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.rubikShopApi.config.VNPConfig;
import com.example.rubikShopApi.entity.Order;
import com.example.rubikShopApi.entity.OrderDetail;
import com.example.rubikShopApi.entity.OrderDetailPrimaryKey;
import com.example.rubikShopApi.entity.PaymentInfo;
import com.example.rubikShopApi.entity.ShippingInfo;
import com.example.rubikShopApi.entity.User;
import com.example.rubikShopApi.request.CartItem;
import com.example.rubikShopApi.request.OrderRequest;
import com.example.rubikShopApi.service.IOrderDetailService;
import com.example.rubikShopApi.service.IOrderService;
import com.example.rubikShopApi.service.IPaymentInfoService;
import com.example.rubikShopApi.service.IUserService;
import com.example.rubikShopApi.service.IVNPService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
	
	@Autowired
	IPaymentInfoService paymentInfoService;

	@PostMapping("/create")
	public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderData, HttpServletRequest request)
			throws UnsupportedEncodingException {
		// Find the user by ID
		User user = userService.findById(orderData.getUserID());

		if (user != null) {
			Order entity = new Order();

			BeanUtils.copyProperties(orderData, entity);

			entity.setUser(user);

			ShippingInfo shipInfo = new ShippingInfo("", orderData.getRoadName(), orderData.getDistrict(),
					orderData.getCity());
			entity.setShippingInfo(shipInfo);

			entity.setOrderDate(LocalDate.now());
			
			orderService.save(entity);

			List<CartItem> cartItems = orderData.getCartItems();

			for (CartItem cartItem : cartItems) {
				OrderDetail orderDetail = new OrderDetail();

				System.out.println(cartItem.getProduct());

				orderDetail.setProduct(cartItem.getProduct());
				orderDetail.setOrder(entity);
				orderDetail.setQuantity(cartItem.getQuantity());

				OrderDetailPrimaryKey primaryKey = new OrderDetailPrimaryKey(entity.getOrderID(),
						cartItem.getProduct().getProductID());
				orderDetail.setId(primaryKey);

				try {
					orderDetailService.save(orderDetail);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (entity.getPaymentMethod().equalsIgnoreCase("VNPAY")) {
//	        	String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
				String vnpayUrl = ivnpService.createOrder(request, entity.getTotalAmount(), String.valueOf(entity.getOrderID()), VNPConfig.vnp_ReturnUrl);
				System.out.println(vnpayUrl);
				return ResponseEntity.status(HttpStatus.OK).body("url:" + vnpayUrl);
			} else {

				return ResponseEntity.ok().body(entity);
			}

		}

		return ResponseEntity.badRequest().body(null);
	}

	@GetMapping("/returnOrder")
    public void paymentCompleted(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int paymentStatus = ivnpService.orderReturn(request);

        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");

        String redirectUrl = "http://localhost:3000/returnOrder?orderID=" + orderInfo;

        if (orderInfo == null || paymentTime == null || transactionId == null || totalPrice == null) {
            response.sendRedirect(redirectUrl + "&transactionStatus=fail");
            return;
        }

        Optional<Order> order = orderService.findById(Integer.valueOf(orderInfo));

        if (order.isPresent()) {
            if (paymentStatus == 1) {
                PaymentInfo info = new PaymentInfo();
                info.setPayContent("Thanh toan cho hoa don " + orderInfo);
                info.setPaymentTime(paymentTime);
                info.setTransactionId(transactionId);
                info.setOrder(order.get());

                paymentInfoService.save(info);

                order.get().setPaymented(true);
                orderService.save(order.get());

                response.sendRedirect(redirectUrl + "&transactionStatus=success");
                return;
            }
        }

        response.sendRedirect(redirectUrl + "&transactionStatus=fail");
    }


}
