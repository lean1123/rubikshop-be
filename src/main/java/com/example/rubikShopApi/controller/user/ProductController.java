package com.example.rubikShopApi.controller.user;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.rubikShopApi.entity.Category;
import com.example.rubikShopApi.entity.Product;
import com.example.rubikShopApi.request.ResponseObject;
import com.example.rubikShopApi.service.ICategoryService;
import com.example.rubikShopApi.service.IProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

	@Autowired
	IProductService productService;

	@Autowired
	ICategoryService categoryService;

	@GetMapping("searchPagination")
	public ResponseEntity<ResponseObject> searchAndPaginating(@RequestParam(defaultValue = "") String searchValue,
			@RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size,
			@RequestParam(defaultValue = "asc") String sortDirector, @RequestParam Optional<Integer> categoryID,
			@RequestParam Optional<Double> startAmount, @RequestParam Optional<Double> endAmount,
			@RequestParam Optional<Boolean> isActive) {

		long count = productService.count();
		int currentPage = page.orElse(0); // Default page is 0 (first page)
		int sizeEachPage = size.orElse(5); // Default size is 5

		String sortDirection = sortDirector;
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by("productName").ascending()
				: Sort.by("productName").descending();

		Pageable pageable = PageRequest.of(currentPage, sizeEachPage, sort);

		Page<Product> result = null;

		if (!searchValue.isBlank() && StringUtils.hasText(searchValue)) {
			result = productService.findByProductNameContaining(searchValue, pageable);
		} else if (categoryID.isPresent() && !categoryID.isEmpty()) {

			Optional<Category> cate = categoryService.findById(categoryID.get());

			if (cate.isPresent())
				result = productService.findByCategory(cate.get(), pageable);

		} else if (startAmount.isPresent() && !startAmount.isEmpty() && endAmount.isPresent() && !endAmount.isEmpty()) {
			result = productService.findByUnitPriceBetween(startAmount.get(), endAmount.get(), pageable);

		} else {
			result = productService.findAll(pageable);
		}

		int totalPages = result.getTotalPages();

		List<Integer> pageNumbers = null;
		if (totalPages > 0) {
			int start = Math.max(0, currentPage - 2);
			int end = Math.min(currentPage + 2, totalPages - 1);

			pageNumbers = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
		}

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(null, "OK", "List video", result));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Product> getProductByID(@PathVariable("id") Optional<Integer> id){
		
		if(id.isPresent()) {
			Optional<Product> opt = productService.findById(id.get());
			
			if(opt.isPresent()) {
				return ResponseEntity.status(HttpStatus.OK).body(opt.get());
			}
			
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		return ResponseEntity.badRequest().body(null);
	}
	
	@GetMapping("")
	public ResponseEntity<?> getAll(){
		return ResponseEntity.ok().body("Success Access!");
	}
}
