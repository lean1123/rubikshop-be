package com.example.rubikShopApi.service.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.rubikShopApi.entity.Order;
import com.example.rubikShopApi.repository.OrderRepository;
import com.example.rubikShopApi.service.IOrderService;

@Service
public class OrderService implements IOrderService {
	@Autowired
	OrderRepository orderRepository;

	public OrderService(OrderRepository orderRepository) {
		// TODO Auto-generated constructor stub
		this.orderRepository = orderRepository;
	}

	@Override
	public <S extends Order> S save(S entity) {
		return orderRepository.save(entity);
	}

	@Override
	public Page<Order> findAll(Pageable pageable) {
		return orderRepository.findAll(pageable);
	}

	@Override
	public List<Order> findAll() {
		return orderRepository.findAll();
	}

	@Override
	public long count() {
		return orderRepository.count();
	}

	@Override
	public void deleteById(Integer id) {
		orderRepository.deleteById(id);
	}

}
