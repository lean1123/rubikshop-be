/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.rubikShopApi.entity;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productId", unique = true, nullable = false)
    private int productID;
    private String productName;
    private String description;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;
    
    @JsonIgnore
    @OneToMany(mappedBy = "product")
    @ToString.Exclude
    private List<OrderDetail> orderDetails;
    
    private double unitPrice;
    private int unitInStock;
    private String image;

    @OneToOne(mappedBy = "product", cascade = CascadeType.MERGE)
    @JsonIgnore
    private CartDetail cartDetail;
    
    private Boolean active;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    @ToString.Exclude
    private List<Review> reviews;
}
