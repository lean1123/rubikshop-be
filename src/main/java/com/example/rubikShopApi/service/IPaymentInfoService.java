package com.example.rubikShopApi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.rubikShopApi.entity.PaymentInfo;

public interface IPaymentInfoService {

	void deleteById(Integer id);

	long count();

	Optional<PaymentInfo> findById(Integer id);

	List<PaymentInfo> findAll();

	Page<PaymentInfo> findAll(Pageable pageable);

	<S extends PaymentInfo> S save(S entity);

}
