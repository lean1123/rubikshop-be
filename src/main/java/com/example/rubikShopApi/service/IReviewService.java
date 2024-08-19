package com.example.rubikShopApi.service;

import com.example.rubikShopApi.entity.Product;
import com.example.rubikShopApi.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface IReviewService {
    <S extends Review> S save(S entity);

    Optional<Review> findById(Integer integer);

    void deleteById(Integer integer);

    void delete(Review entity);

    List<Review> findAll(Sort sort);

    Page<Review> findAll(Pageable pageable);

    Page<Review> findAllByProduct(Product product, Pageable pageable);
}
