package com.example.rubikShopApi.request;

import java.util.List;

import org.springframework.web.bind.annotation.BindParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.rubikShopApi.entity.CartDetail;
import com.example.rubikShopApi.entity.Category;
import com.example.rubikShopApi.entity.OrderDetail;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductModel {
	
    private int productID;
    
    private String productName;
    
    private String description;

    
    private int categoryID;
    
    private double unitPrice;
    private int unitInStock;
    
    private String imageUrl;
    
   @Null
    private MultipartFile imageFile;
  
    private Boolean active;
    
    private Boolean isResource;
    
    private Boolean isEdit = false;

    private String additionalInfo;

}
