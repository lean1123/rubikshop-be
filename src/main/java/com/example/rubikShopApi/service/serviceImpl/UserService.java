package com.example.rubikShopApi.service.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.rubikShopApi.entity.User;
import com.example.rubikShopApi.repository.UserRepository;
import com.example.rubikShopApi.service.IUserService;

@Service
@EnableMethodSecurity
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
	@PreAuthorize("hasRole('ADMIN')")
	public Page<User> findAll(Pageable pageable) {
		return userRepo.findAll(pageable);
	}

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public List<User> findAll() {
		return userRepo.findAll();
	}

	@Override
	@PostAuthorize("returnObject.email == authentication.name")
	public User findById(Integer id) {
		
		var result = userRepo.findById(id);
		
		return result.isPresent() ? result.get() : null;
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

	@Override
	@PostAuthorize("returnObject.email == authentication.name")
	public User getMyInfo(){
		var context = SecurityContextHolder.getContext();
		
		String email = context.getAuthentication().getName();
		
		System.out.println(email);
		
		Optional<User> opt = userRepo.findByEmail(email);
		
		if(opt.isPresent()) {
			return opt.get();
		}
		
		return null;
	}
}
