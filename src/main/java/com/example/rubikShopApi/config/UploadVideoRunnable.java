package com.example.rubikShopApi.config;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Component
@Scope("prototype")
public class UploadVideoRunnable implements Callable<String> {

    private final MultipartFile image;
    private final Cloudinary cloudinary;

    public UploadVideoRunnable(MultipartFile image, Cloudinary cloudinary) {
        this.image = image;
        this.cloudinary = cloudinary;
    }

    @SuppressWarnings("unchecked")
	@Override
    public String call() throws Exception {
        try {
            Map<String, Object> cloudinaryResult = cloudinary.uploader().upload(image.getBytes(),
                    ObjectUtils.asMap("resource_type", "auto"));
            return (String) cloudinaryResult.get("secure_url");
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("Upload failed", e);
        }
    }
}
