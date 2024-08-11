package com.example.rubikShopApi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.rubikShopApi.entity.Order;

public interface IOrderService {

	void deleteById(Integer id);

	long count();

	List<Order> findAll();

	Page<Order> findAll(Pageable pageable);

	<S extends Order> S save(S entity);

	Optional<Order> findById(Integer id);

    Page<Order> findByUser(Pageable pageable);
}
