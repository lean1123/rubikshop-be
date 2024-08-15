package com.example.rubikShopApi.controller.user;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.rubikShopApi.entity.Rating;
import com.example.rubikShopApi.entity.Review;
import com.example.rubikShopApi.request.ReviewRequest;
import com.example.rubikShopApi.service.IProductService;
import com.example.rubikShopApi.service.IReviewService;
import com.example.rubikShopApi.service.IStorageService;
import com.example.rubikShopApi.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("reviews")
public class ReviewController {

    @Autowired
    IReviewService reviewService;

    @Autowired
    IUserService userService;

    @Autowired
    IProductService productService;

    @Autowired
    Cloudinary cloudinary;

    @GetMapping("getAllPageable")
    public ResponseEntity<?> getAllPageable(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "9") int size, @RequestParam(defaultValue = "asc") String sort) {

        Sort sortDirection = sort.equalsIgnoreCase("asc") ? Sort.by("updatedDate").ascending()
                : Sort.by("updatedDate").descending();

        Pageable pageable = PageRequest.of(page, size, sortDirection);

        return ResponseEntity.ok().body(reviewService.findAll(pageable));
    }

    @PostMapping(value = "addReview", consumes = {"multipart/form-data"})
    public ResponseEntity<?> addReview(@ModelAttribute ReviewRequest request, BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.toString());
        }

        Review entity = new Review();

        var user = userService.findById(request.getUserID());

        if(user == null){
            return ResponseEntity.badRequest().body("Error: User isn't exist!");
        }

        entity.setUser(user);

        var product = productService.findById(request.getProductID());

        if(product.isEmpty()){
            return ResponseEntity.badRequest().body("Error: Product isn't exist!");
        }

        product.ifPresent(entity::setProduct);

        var listFile = request.getListImageFile();

        List<String> listImageUrl = null;
        if (!(listFile.length == 0)) {
            listImageUrl = new ArrayList<>();
            try {
                for(int i =0; i<request.getListImageFile().length; i++){
                    Map cloudinaryResult = cloudinary.uploader().upload(listFile[i].getBytes(),
                            ObjectUtils.asMap("resource_type", "auto"));

                    String imageUrl = (String) cloudinaryResult.get("secure_url");
                    listImageUrl.add(imageUrl);
                }

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return ResponseEntity.badRequest().body("Error while upload file from server!");
            }
        }

        entity.setListImageReview(listImageUrl);

        entity.setComment(request.getComment());

        entity.setRating(Rating.fromValue(request.getRating()));

        entity.setUpdatedDate(new Date());

        reviewService.save(entity);

        return ResponseEntity.ok().body("Insert success");
    }

}
