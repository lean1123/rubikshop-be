package com.example.rubikShopApi.request;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
