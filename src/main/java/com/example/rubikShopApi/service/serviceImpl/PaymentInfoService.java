package com.example.rubikShopApi.service.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.rubikShopApi.entity.Order;
import com.example.rubikShopApi.entity.PaymentInfo;
import com.example.rubikShopApi.repository.PaymentInfoRepository;
import com.example.rubikShopApi.service.IPaymentInfoService;

@Service
public class PaymentInfoService implements IPaymentInfoService{
	@Autowired
	PaymentInfoRepository paymentInfoRepository;

	public PaymentInfoService(PaymentInfoRepository paymentInfoRepository) {
		this.paymentInfoRepository = paymentInfoRepository;
	}

	@Override
	public <S extends PaymentInfo> S save(S entity) {
		return paymentInfoRepository.save(entity);
	}

	@Override
	public Page<PaymentInfo> findAll(Pageable pageable) {
		return paymentInfoRepository.findAll(pageable);
	}

	@Override
	public List<PaymentInfo> findAll() {
		return paymentInfoRepository.findAll();
	}

	@Override
	public Optional<PaymentInfo> findById(Integer id) {
		return paymentInfoRepository.findById(id);
	}

	@Override
	public long count() {
		return paymentInfoRepository.count();
	}

	@Override
	public void deleteById(Integer id) {
		paymentInfoRepository.deleteById(id);
	}

	@Override
	public Optional<PaymentInfo> findByOrder(Order order) {
		return paymentInfoRepository.findByOrder(order);
	}
	
	
}
