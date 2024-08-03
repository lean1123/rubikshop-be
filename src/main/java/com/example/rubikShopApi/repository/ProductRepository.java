package com.example.rubikShopApi.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.rubikShopApi.entity.Category;
import com.example.rubikShopApi.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{
	Page<Product> findByProductNameContaining(String productName, Pageable pageable);
	List<Product> findByProductNameContaining(String productName);
	Page<Product> findByCategory(Category categoryID, Pageable pageable);
	Page<Product> findByUnitPriceBetween(double fromAmount, double toAmount, Pageable pageable);
	Page<Product> findByActive(boolean active, Pageable pageable);


}
