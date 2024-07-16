package com.example.rubikShopApi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.rubikShopApi.entity.Category;

public interface ICategoryService {

	<S extends Category> S save(S entity);

	void deleteById(Integer id);

	long count();

	Optional<Category> findById(Integer id);

	List<Category> findAll();

	Page<Category> findAll(Pageable pageable);

	Page<Category> findByCategoryNameContaining(String string, Pageable pageable);

	List<Category> findByCategoryNameContaining(String string);

}
