package com.example.rubikShopApi.repository;

import com.example.rubikShopApi.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.rubikShopApi.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>{
    Page<Order> findByUser(User user, Pageable pageable);
}
