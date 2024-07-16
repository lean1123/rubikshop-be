package com.example.rubikShopApi.service.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.rubikShopApi.entity.Category;
import com.example.rubikShopApi.entity.Product;
import com.example.rubikShopApi.repository.ProductRepository;
import com.example.rubikShopApi.service.IProductService;

@Service
public class ProductService implements IProductService {
	@Autowired
	ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		// TODO Auto-generated constructor stub
		this.productRepository = productRepository;
	}

	@Override
	public <S extends Product> S save(S entity) {
		return productRepository.save(entity);
	}

	@Override
	public Page<Product> findAll(Pageable pageable) {
		return productRepository.findAll(pageable);
	}

	@Override
	public List<Product> findAll() {
		return productRepository.findAll();
	}

	@Override
	public long count() {
		return productRepository.count();
	}

	@Override
	public void deleteById(Integer id) {
		productRepository.deleteById(id);
	}

	@Override
	public Page<Product> findByProductNameContaining(String productName, Pageable pageable) {
		return productRepository.findByProductNameContaining(productName, pageable);
	}

	@Override
	public Page<Product> findByCategory(Category categoryID, Pageable pageable) {
		return productRepository.findByCategory(categoryID, pageable);
	}

	@Override
	public Page<Product> findByUnitPriceBetween(double fromAmount, double toAmount, Pageable pageable) {
		return productRepository.findByUnitPriceBetween(fromAmount, toAmount, pageable);
	}

	@Override
	public Optional<Product> findById(Integer id) {
		return productRepository.findById(id);
	}

	@Override
	public List<Product> findByProductNameContaining(String productName) {
		return productRepository.findByProductNameContaining(productName);
	}

	@Override
	public Page<Product> findByActive(boolean active, Pageable pageable) {
		return productRepository.findByActive(active, pageable);
	}

}
