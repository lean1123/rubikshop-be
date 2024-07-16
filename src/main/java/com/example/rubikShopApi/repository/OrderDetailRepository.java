package com.example.rubikShopApi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.rubikShopApi.entity.OrderDetail;
import com.example.rubikShopApi.entity.OrderDetailPrimaryKey;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailPrimaryKey>{

}
