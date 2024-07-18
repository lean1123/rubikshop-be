package com.example.rubikShopApi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rubikShopApi.entity.PaymentInfo;

public interface PaymentInfoRepository extends JpaRepository<PaymentInfo, Integer>{

}
