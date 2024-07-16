package com.example.rubikShopApi.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.rubikShopApi.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>{
	List<Category> findByCategoryNameContaining(String string);
	Page<Category> findByCategoryNameContaining(String string, Pageable pageable);
}
