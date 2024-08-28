/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.rubikShopApi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "categories")
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "categoryId", unique = true, nullable = false)
	private int categoryID;
	private String categoryName;
	private String categoryImage;

	@ToString.Exclude
	@JsonIgnore
	@OneToMany(mappedBy = "category", cascade = { CascadeType.MERGE })
	private List<Product> products;

	public Category(int categoryID, String categoryName, String categoryImage) {
		this.categoryID = categoryID;
		this.categoryName = categoryName;
		this.categoryImage = categoryImage;
	}

}
