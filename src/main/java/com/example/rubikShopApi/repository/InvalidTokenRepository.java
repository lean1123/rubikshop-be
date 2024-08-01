package com.example.rubikShopApi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rubikShopApi.entity.InvalidToken;

public interface InvalidTokenRepository extends JpaRepository<InvalidToken, String>{

}
