package com.example.rubikShopApi.service.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.rubikShopApi.entity.Order;
import com.example.rubikShopApi.entity.OrderDetail;
import com.example.rubikShopApi.entity.OrderDetailPrimaryKey;
import com.example.rubikShopApi.entity.Product;
import com.example.rubikShopApi.repository.OrderDetailRepository;
import com.example.rubikShopApi.repository.ProductRepository;
import com.example.rubikShopApi.service.IOrderDetailService;

@Service
public class OrderDetailService implements IOrderDetailService {
	@Autowired
	OrderDetailRepository orderDetailRepository;

	@Autowired
	ProductRepository productRepository;

	public OrderDetailService(OrderDetailRepository orderDetailRepository) {
		// TODO Auto-generated constructor stub
		this.orderDetailRepository = orderDetailRepository;
	}

	@Override
	public <S extends OrderDetail> S save(S entity) throws Exception {

		Product orderProd = entity.getProduct();

		int numOfStock = orderProd.getUnitInStock();

		int numOfOrder = entity.getQuantity();
		if (numOfStock <= 0 || numOfStock < numOfOrder) {
			throw new Exception("This number of product is not enough");
		}

		orderProd.setUnitInStock(numOfStock - numOfOrder);

		try {
			productRepository.save(orderProd);
			return orderDetailRepository.save(entity);
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
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
