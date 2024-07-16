package com.example.rubikShopApi.service.serviceImpl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.stereotype.Service;

import com.example.rubikShopApi.config.VNPConfig;
import com.example.rubikShopApi.service.IVNPService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
public class VNPService implements IVNPService{
	
	
	
	
	@Override
	public String createOrder(HttpServletRequest request, double amount, String orderInfor, String urlReturn) throws UnsupportedEncodingException {
		 String vnp_Version = "2.1.0";
	        String vnp_Command = "pay";
	        String orderType = "order-type";
	        
	        String bankCode = "VNBANK";
	        
	        String vnp_TxnRef = VNPConfig.getRandomNumber(8);
	        String vnp_IpAddr = "192.168.1.4";

	        String vnp_TmnCode = VNPConfig.vnp_TmnCode;
	        
	        Map<String, String> vnp_Params = new HashMap<>();
	        vnp_Params.put("vnp_Version", vnp_Version);
	        vnp_Params.put("vnp_Command", vnp_Command);
	        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
	        vnp_Params.put("vnp_Amount", String.valueOf(((int) amount * 100)));
	        vnp_Params.put("vnp_CurrCode", "VND");
	        
	        if (bankCode != null && !bankCode.isEmpty()) {
	            vnp_Params.put("vnp_BankCode", bankCode);
	        }
	        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
	        vnp_Params.put("vnp_OrderInfo", "Nap tien cho thue bao 0123456789. So tien 100,000 VND");
	        vnp_Params.put("vnp_OrderType", orderType);

	        String locate = "vn";
	        if (locate != null && !locate.isEmpty()) {
	            vnp_Params.put("vnp_Locale", locate);
	        } else {
	            vnp_Params.put("vnp_Locale", "vn");
	        }
	        vnp_Params.put("vnp_ReturnUrl", VNPConfig.vnp_ReturnUrl);
	        vnp_Params.put("vnp_IpAddr", VNPConfig.getIpAddress(request));

	        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
	        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
	        String vnp_CreateDate = formatter.format(cld.getTime());
	        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
	        
	        cld.add(Calendar.MINUTE, 15);
	        String vnp_ExpireDate = formatter.format(cld.getTime());
	        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
	        
	        List fieldNames = new ArrayList(vnp_Params.keySet());
	        Collections.sort(fieldNames);
	        StringBuilder hashData = new StringBuilder();
	        StringBuilder query = new StringBuilder();
	        Iterator itr = fieldNames.iterator();
	        while (itr.hasNext()) {
	            String fieldName = (String) itr.next();
	            String fieldValue = (String) vnp_Params.get(fieldName);
	            if ((fieldValue != null) && (fieldValue.length() > 0)) {
	                //Build hash data
	                hashData.append(fieldName);
	                hashData.append('=');
	                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
	                //Build query
	                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
	                query.append('=');
	                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
	                if (itr.hasNext()) {
	                    query.append('&');
	                    hashData.append('&');
	                }
	            }
	        }
	        String queryUrl = query.toString();
//	        VNPConfig.hmacSHA512(VNPConfig.secretKey, hashData.toString());
	        String vnp_SecureHash = VNPConfig.hmacSHA512(VNPConfig.secretKey, hashData.toString());
	        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
	        String paymentUrl = VNPConfig.vnp_PayUrl + "?" + queryUrl;
	        return paymentUrl;
	}

	@Override
	public int orderReturn(HttpServletRequest request) {
		Map fields = new HashMap();
		for (Enumeration params = request.getParameterNames(); params.hasMoreElements();) {
			String fieldName = null;
			String fieldValue = null;
			try {
				fieldName = URLEncoder.encode((String) params.nextElement(), StandardCharsets.US_ASCII.toString());
				fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII.toString());
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			if ((fieldValue != null) && (fieldValue.length() > 0)) {
				fields.put(fieldName, fieldValue);
			}
		}

		String vnp_SecureHash = request.getParameter("vnp_SecureHash");
		if (fields.containsKey("vnp_SecureHashType")) {
			fields.remove("vnp_SecureHashType");
		}
		if (fields.containsKey("vnp_SecureHash")) {
			fields.remove("vnp_SecureHash");
		}
		String signValue = VNPConfig.hashAllFields(fields);
		if (signValue.equals(vnp_SecureHash)) {
			if ("00".equals(request.getParameter("vnp_TransactionStatus"))) {
				return 1;
			} else {
				return 0;
			}
		} else {
			return -1;
		}
	}
}
