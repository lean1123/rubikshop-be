package com.example.rubikShopApi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString

@Table(name="reviews")
public class Review {
    @Id
    @Column(name = "ratingID", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "userID", referencedColumnName = "userID")
    private User user;
    @ManyToOne
    @JoinColumn(name = "productId", referencedColumnName = "productId")
    private Product product;

    private String comment;

    @Enumerated
    private Rating rating;

    @ElementCollection
    @JoinTable(name = "listImageUrl", joinColumns = @JoinColumn(name = "rating_id"))
    private List<String> listImageReview;

    private Date updatedDate;

}
