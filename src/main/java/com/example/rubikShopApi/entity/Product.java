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
    @OneToMany(mappedBy = "product", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private List<OrderDetail> orderDetails;
    
    private double unitPrice;
    private int unitInStock;
    private String image;
    
    @JsonIgnore
    @OneToOne(mappedBy = "product")
    private CartDetail cartDetail;
    
    private Boolean active;
}
