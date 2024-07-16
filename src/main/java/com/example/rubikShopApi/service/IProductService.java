package com.example.rubikShopApi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.rubikShopApi.entity.Category;
import com.example.rubikShopApi.entity.Product;

public interface IProductService {

	void deleteById(Integer id);

	long count();

	List<Product> findAll();

	Page<Product> findAll(Pageable pageable);

	<S extends Product> S save(S entity);

	Page<Product> findByProductNameContaining(String productName, Pageable pageable);

	Page<Product> findByUnitPriceBetween(double fromAmount, double toAmount, Pageable pageable);

	Page<Product> findByCategory(Category categoryID, Pageable pageable);

	Optional<Product> findById(Integer id);

	List<Product> findByProductNameContaining(String productName);

	Page<Product> findByActive(boolean active, Pageable pageable);

}
