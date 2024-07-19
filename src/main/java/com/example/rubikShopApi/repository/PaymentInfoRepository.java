package com.example.rubikShopApi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rubikShopApi.entity.Order;
import com.example.rubikShopApi.entity.PaymentInfo;

public interface PaymentInfoRepository extends JpaRepository<PaymentInfo, Integer>{
	Optional<PaymentInfo> findByOrder(Order order);
}
