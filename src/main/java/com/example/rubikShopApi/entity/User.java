/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.rubikShopApi.entity;

import java.lang.reflect.Member;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userID;
	private String fullName;
	
	@Enumerated(EnumType.STRING)
	private Role role;

	private String password;
	private String userImg;
	
	@Column(unique = true, nullable = false)
	private String email;
	private String address;

	@JsonIgnore
	@ToString.Exclude
	@OneToMany(mappedBy = "user", cascade = {CascadeType.MERGE})
	private List<Order> orders;
	
	@JsonIgnore
	@OneToOne(mappedBy = "user")
	private Cart cart;

	@JsonIgnore
	@OneToMany(mappedBy = "user", cascade = CascadeType.MERGE)
	private List<Review> reviews;
}