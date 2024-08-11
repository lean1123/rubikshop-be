package com.example.rubikShopApi.service.serviceImpl;

import java.util.List;
import java.util.Optional;

import com.example.rubikShopApi.entity.User;
import com.example.rubikShopApi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.rubikShopApi.entity.Order;
import com.example.rubikShopApi.repository.OrderRepository;
import com.example.rubikShopApi.service.IOrderService;

@Service
public class OrderService implements IOrderService {
	@Autowired
	OrderRepository orderRepository;

	@Autowired
	UserRepository userRepository;

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

	@Override
	public Optional<Order> findById(Integer id) {
		return orderRepository.findById(id);
	}

	@Override
	public Page<Order> findByUser(Pageable pageable) {
		var context = SecurityContextHolder.getContext();

		String email = context.getAuthentication().getName();

		Optional<User> opt = userRepository.findByEmail(email);
        return opt.map(user -> orderRepository.findByUser(user
                , pageable)).orElse(null);

    }
}
