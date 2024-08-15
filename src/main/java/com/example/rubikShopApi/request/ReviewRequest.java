package com.example.rubikShopApi.request;

import com.example.rubikShopApi.entity.Product;
import com.example.rubikShopApi.entity.Rating;
import com.example.rubikShopApi.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequest {
    private Integer id;
    private Integer userID;

    private Integer productID;

    @Null
    private String comment;


    private Integer rating;


    @Null
    private MultipartFile[] listImageFile;
}
