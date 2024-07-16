package com.example.rubikShopApi.service.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.rubikShopApi.entity.Order;
import com.example.rubikShopApi.entity.OrderDetail;
import com.example.rubikShopApi.entity.OrderDetailPrimaryKey;
import com.example.rubikShopApi.repository.OrderDetailRepository;
import com.example.rubikShopApi.service.IOrderDetailService;

@Service
public class OrderDetailService implements IOrderDetailService {
	@Autowired
	OrderDetailRepository orderDetailRepository;

	public OrderDetailService(OrderDetailRepository orderDetailRepository) {
		// TODO Auto-generated constructor stub
		this.orderDetailRepository = orderDetailRepository;
	}

	@Override
	public <S extends OrderDetail> S save(S entity) {
		return orderDetailRepository.save(entity);
	}

	@Override
	public List<OrderDetail> findAll() {
		return orderDetailRepository.findAll();
	}

	@Override
	public long count() {
		return orderDetailRepository.count();
	}

	

	@Override
	public void delete(OrderDetail entity) {
		orderDetailRepository.delete(entity);
	}

	@Override
	public void deleteAll() {
		orderDetailRepository.deleteAll();
	}

}
