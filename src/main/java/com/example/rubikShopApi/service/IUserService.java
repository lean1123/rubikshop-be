package com.example.rubikShopApi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import com.example.rubikShopApi.entity.User;

public interface IUserService {

	void deleteById(Integer id);

	long count();

	boolean existsById(Integer id);

	User findById(Integer id);

	List<User> findAll();

	Page<User> findAll(Pageable pageable);

	<S extends User> S save(S entity);

	Optional<User> findByEmail(String email);

	User getMyInfo();

}
