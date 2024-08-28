package com.example.rubikShopApi.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/cloudinary-server")
public class CloudinaryController {

    @Autowired
    Cloudinary cloudinary;

    @PostMapping(value = "/upload-image", consumes = {"multipart/form-data"})
    public ResponseEntity<?> uploadImage(MultipartFile file){
        try {

            Map cloudinaryResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("resource_type", "auto"));

            String imageUrl = (String) cloudinaryResult.get("secure_url");

            return ResponseEntity.ok().body(imageUrl);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error while uploading image!");
        }
    }
}
