package com.example.rubikShopApi.service;

import java.util.List;
import java.util.Optional;

import com.example.rubikShopApi.entity.Order;
import com.example.rubikShopApi.entity.OrderDetail;
import com.example.rubikShopApi.entity.OrderDetailPrimaryKey;

public interface IOrderDetailService {

	List<OrderDetail> findAll();

	void deleteAll();

	void delete(OrderDetail entity);

	long count();

	<S extends OrderDetail> S save(S entity);
}
