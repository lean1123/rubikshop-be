package com.example.rubikShopApi.request;

import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Null;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryModel {
    private int categoryID;
    private String categoryCode;

    @NotEmpty
    @Length(min = 5)
    private String categoryName;
    private String images;
    @Null
    private MultipartFile imageFile;
    private boolean status;
    private Boolean isResource = false;
    private Boolean isEdit = false;
    
}
