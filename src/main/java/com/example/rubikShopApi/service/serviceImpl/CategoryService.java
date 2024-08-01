package com.example.rubikShopApi.service.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.example.rubikShopApi.entity.Category;
import com.example.rubikShopApi.repository.CategoryRepository;
import com.example.rubikShopApi.service.ICategoryService;

@Service
public class CategoryService implements ICategoryService {
	@Autowired
	CategoryRepository cateRepo;

	public CategoryService(CategoryRepository categoryRepository) {
		// TODO Auto-generated constructor stub
		this.cateRepo = categoryRepository;
	}

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public <S extends Category> S save(S entity) {
		return cateRepo.save(entity);
	}

	@Override
	public Page<Category> findAll(Pageable pageable) {
		return cateRepo.findAll(pageable);
	}

	@Override
	public List<Category> findAll() {
		return cateRepo.findAll();
	}

	@Override
	public Optional<Category> findById(Integer id) {
		return cateRepo.findById(id);
	}

	@Override
	public long count() {
		return cateRepo.count();
	}

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public void deleteById(Integer id) {
		cateRepo.deleteById(id);
	}

	@Override
	public List<Category> findByCategoryNameContaining(String string) {
		return cateRepo.findByCategoryNameContaining(string);
	}

	@Override
	public Page<Category> findByCategoryNameContaining(String string, Pageable pageable) {
		return cateRepo.findByCategoryNameContaining(string, pageable);
	}

}
