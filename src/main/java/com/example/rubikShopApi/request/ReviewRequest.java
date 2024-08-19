package com.example.rubikShopApi.request;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequest {
    private Integer id;
    private Integer userID;

    private Integer productID;


    private String comment;


    @NotNull
    private Integer rating;


    private MultipartFile[] listImageFile;
}
