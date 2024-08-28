package com.example.rubikShopApi.controller.user;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.rubikShopApi.entity.Rating;
import com.example.rubikShopApi.entity.Review;
import com.example.rubikShopApi.request.ReviewRequest;
import com.example.rubikShopApi.service.IOrderService;
import com.example.rubikShopApi.service.IProductService;
import com.example.rubikShopApi.service.IReviewService;
import com.example.rubikShopApi.service.IUserService;
import com.example.rubikShopApi.service.serviceImpl.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

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

    @Autowired
    IOrderService orderService;

    @GetMapping("getAllPageable")
    public ResponseEntity<?> getAllPageable(@RequestParam Integer productID, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "9") int size, @RequestParam(defaultValue = "dsc") String sort) {

        Sort sortDirection = sort.equalsIgnoreCase("asc") ? Sort.by("updatedDate").ascending()
                : Sort.by("updatedDate").descending();

        Pageable pageable = PageRequest.of(page, size, sortDirection);

        var product = productService.findById(productID);

        if (product.isEmpty()) {
            return ResponseEntity.internalServerError().body("Error: Can't get product!");
        }

        return ResponseEntity.ok().body(reviewService.findAllByProduct(product.get(), pageable));
    }

//    @PostMapping(value = "addReview", consumes = {"multipart/form-data"})
//    public ResponseEntity<?> addReview(@Valid @ModelAttribute ReviewRequest request, BindingResult result) {
//
//        if (result.hasErrors()) {
//            return ResponseEntity.badRequest().body(result.toString());
//        }
//
//        Review entity = new Review();
//
//        var user = userService.findById(request.getUserID());
//
//        if (user == null) {
//            return ResponseEntity.badRequest().body("Error: User isn't exist!");
//        }
//
//        entity.setUser(user);
//
//        var product = productService.findById(request.getProductID());
//
//        if (product.isEmpty()) {
//            return ResponseEntity.badRequest().body("Error: Product isn't exist!");
//        }
//
//        product.ifPresent(entity::setProduct);
//
//        var listFile = request.getListImageFile();
//
//        if (listFile == null || listFile.length == 0) {
//            entity.setListImageReview(null);
//        } else {
//
//            AtomicReference<List<String>> listImageUrl = new AtomicReference<>(new ArrayList<>());
//
//            Thread uploadListImage = getThread(listFile, listImageUrl);
//
//            try {
//                uploadListImage.join();
//                entity.setListImageReview(listImageUrl.get());
//            } catch (InterruptedException e) {
//                // Handle thread interruption.
//                e.printStackTrace();
//                return ResponseEntity.badRequest().body("Error while uploading list image!");
//            }
//
//        }
//
//
//        entity.setComment(request.getComment());
//
//        entity.setRating(Rating.fromValue(request.getRating()));
//
//        entity.setUpdatedDate(new Date());
//
//        reviewService.save(entity);
//
//        return ResponseEntity.ok().body(entity);
//    }

//    private Thread getThread(MultipartFile[] listFile, AtomicReference<List<String>> listImageUrl) {
//        Thread uploadListImage = new Thread(() -> {
//
//            Arrays.stream(listFile).parallel().forEach(multipartFile ->
//            {
//                try {
//                    listImageUrl.get().add(getImageUrl(multipartFile).get(3000, TimeUnit.MICROSECONDS));
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                } catch (ExecutionException e) {
//                    throw new RuntimeException(e);
//                } catch (TimeoutException e) {
//                    throw new RuntimeException(e);
//                }
//
//            });
//
//        });
//        uploadListImage.start();
//        return uploadListImage;
//    }

    @GetMapping("possibleReview")
    public ResponseEntity<?> checkPossibleRating(@RequestParam(required = true) Integer userID, @RequestParam(required = true) Integer productID) {
        var user = userService.findById(userID);
        var product = productService.findById(productID);

        if(product.isEmpty()){
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).body("Product isn't existed!");
        }

        var orderList = orderService.findOrdersByUser(user);
        var listOrderDetais = orderService.getAllOrderDetaisFromListOrders(orderList);

        var isPossible = orderService.checkProductIsExistedInListOrder(listOrderDetais, product.get());

        return  ResponseEntity.ok().body(isPossible);
    }

    @PostMapping(value = "addReview", consumes = {"multipart/form-data"})
    public ResponseEntity<?> addReview(@Valid @ModelAttribute ReviewRequest request, BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.toString());
        }

        Review entity = new Review();

        var user = userService.findById(request.getUserID());

        if (user == null) {
            return ResponseEntity.badRequest().body("Error: User isn't exist!");
        }

        entity.setUser(user);

        var product = productService.findById(request.getProductID());

        if (product.isEmpty()) {
            return ResponseEntity.badRequest().body("Error: Product isn't exist!");
        }

        product.ifPresent(entity::setProduct);

        var listFile = request.getListImageFile();

        if (listFile == null || listFile.length == 0) {
            entity.setListImageReview(null);
        } else {
            entity.setListImageReview(getListImageUrl(request.getListImageFile()));
        }

        entity.setComment(request.getComment());
        entity.setRating(Rating.fromValue(request.getRating()));
        entity.setUpdatedDate(new Date());

        reviewService.save(entity);

        return ResponseEntity.ok().body(entity);
    }

    private List<String> getListImageUrl(MultipartFile[] multipartFiles) {

        List<String> resultList = new ArrayList<>();

        Arrays.stream(multipartFiles).forEach(multipartFile -> {
            resultList.add(getImageUrl(multipartFile));
        });

        return resultList;
    }

    private String getImageUrl(MultipartFile multipartFile) {
        try {
            Map<?, ?> cloudinaryResult = cloudinary.uploader().upload(multipartFile.getBytes(),
                    ObjectUtils.asMap("resource_type", "auto"));
            return (String) cloudinaryResult.get("secure_url");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
