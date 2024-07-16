package com.example.rubikShopApi.service.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.rubikShopApi.entity.User;
import com.example.rubikShopApi.repository.UserRepository;
import com.example.rubikShopApi.service.IUserService;

@Service
public class UserService implements IUserService {
	@Autowired
	UserRepository userRepo;

	public UserService(UserRepository userRepository) {
		this.userRepo = userRepository;
	}

	@Override
	public <S extends User> S save(S entity) {
		return userRepo.save(entity);
	}

	@Override
	public Page<User> findAll(Pageable pageable) {
		return userRepo.findAll(pageable);
	}

	@Override
	public List<User> findAll() {
		return userRepo.findAll();
	}

	@Override
	public Optional<User> findById(Integer id) {
		return userRepo.findById(id);
	}

	@Override
	public boolean existsById(Integer id) {
		return userRepo.existsById(id);
	}

	@Override
	public long count() {
		return userRepo.count();
	}

	@Override
	public void deleteById(Integer id) {
		userRepo.deleteById(id);
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return userRepo.findByEmail(email);
	}

	
}
