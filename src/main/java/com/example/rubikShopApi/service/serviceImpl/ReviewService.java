package com.example.rubikShopApi.service.serviceImpl;

import com.example.rubikShopApi.entity.Review;
import com.example.rubikShopApi.repository.ReviewRepository;
import com.example.rubikShopApi.service.IReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService implements IReviewService {

    @Autowired
    ReviewRepository reviewRepository;

    @Override
    public <S extends Review> S save(S entity) {



        return reviewRepository.save(entity);
    }

    @Override
    public Optional<Review> findById(Integer integer) {
        return reviewRepository.findById(integer);
    }

    @Override
    public void deleteById(Integer integer) {
        reviewRepository.deleteById(integer);
    }

    @Override
    public void delete(Review entity) {
        reviewRepository.delete(entity);
    }

    @Override
    public List<Review> findAll(Sort sort) {
        return reviewRepository.findAll(sort);
    }

    @Override
    public Page<Review> findAll(Pageable pageable) {
        return reviewRepository.findAll(pageable);
    }
}
