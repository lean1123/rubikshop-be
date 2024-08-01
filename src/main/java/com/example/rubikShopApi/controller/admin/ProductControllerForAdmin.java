package com.example.rubikShopApi.controller.admin;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.rubikShopApi.entity.Category;
import com.example.rubikShopApi.entity.Product;
import com.example.rubikShopApi.request.CategoryModel;
import com.example.rubikShopApi.request.ProductModel;
import com.example.rubikShopApi.request.ResponseObject;
import com.example.rubikShopApi.service.ICategoryService;
import com.example.rubikShopApi.service.IProductService;
import com.example.rubikShopApi.service.IStorageService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin/products")
public class ProductControllerForAdmin {

	@Autowired
	IProductService productService;

	@Autowired
	ICategoryService categoryService;

	@Autowired
	IStorageService storageService;

	@Autowired
	Cloudinary cloudinary;

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
	public ResponseEntity<Product> getProductByID(@PathVariable("id") Optional<Integer> id) {

		if (id.isPresent()) {
			Optional<Product> opt = productService.findById(id.get());

			if (opt.isPresent()) {
				return ResponseEntity.status(HttpStatus.OK).body(opt.get());
			}

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		return ResponseEntity.badRequest().body(null);
	}

	@PostMapping("/save")
	public ResponseEntity<Product> saveOrUpdate(@ModelAttribute ProductModel productModel,
			BindingResult result) {

		if (result.hasErrors()) {
			System.out.println(result.toString());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(null);
		}

		Product entity = new Product();

//		coppy model sang entity
		BeanUtils.copyProperties(productModel, entity);

		int categoryID = productModel.getCategoryID();

		Optional<Category> opt = categoryService.findById(categoryID);

		Category category = null;

		if (opt.isPresent()) {
			category = opt.get();
		}

		entity.setCategory(category);

		if (!productModel.getImageFile().isEmpty()) {
			if (productModel.getIsResource() == true) {
				UUID uuID = UUID.randomUUID();
				String uuIDString = uuID.toString();
				entity.setImage(storageService.getStorageFileName(productModel.getImageFile(), uuIDString));
				storageService.store(productModel.getImageFile(), entity.getImage());
			} else {
				try {

					Map cloudinaryResult = cloudinary.uploader().upload(productModel.getImageFile().getBytes(),
							ObjectUtils.asMap("resource_type", "auto"));

					String imageUrl = (String) cloudinaryResult.get("secure_url");

					entity.setImage(imageUrl);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		productService.save(entity);

		return ResponseEntity.status(HttpStatus.OK).body(entity);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<Product> update(@PathVariable("id") int id, @ModelAttribute ProductModel resource) {
	    System.out.println(id);

	    Optional<Product> opt = productService.findById(id);

	    if (opt.isPresent()) {
	        Product currentSearch = opt.get();

	        // Sao chép thuộc tính từ resource
	        currentSearch.setProductName(resource.getProductName());
	        currentSearch.setDescription(resource.getDescription());
	        currentSearch.setUnitInStock(resource.getUnitInStock());
	        currentSearch.setUnitPrice(resource.getUnitPrice());
	        currentSearch.setActive(resource.getActive());
	        currentSearch.setCategory(categoryService.findById(resource.getCategoryID()).orElse(null));

	        // Xử lý imageFile
	        if (resource.getImageFile() != null && !resource.getImageFile().isEmpty()) {
	            if (resource.getIsResource()) {
	                UUID uuID = UUID.randomUUID();
	                String uuIDString = uuID.toString();
	                currentSearch.setImage(storageService.getStorageFileName(resource.getImageFile(), uuIDString));
	                storageService.store(resource.getImageFile(), currentSearch.getImage());
	            } else {
	                try {
	                    Map cloudinaryResult = cloudinary.uploader().upload(resource.getImageFile().getBytes(),
	                            ObjectUtils.asMap("resource_type", "auto"));
	                    String imageUrl = (String) cloudinaryResult.get("secure_url");
	                    currentSearch.setImage(imageUrl);
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        } else {
	            currentSearch.setImage(currentSearch.getImage());
	        }

	        System.out.println(currentSearch.toString());

	        productService.save(currentSearch);
	        return ResponseEntity.ok().body(currentSearch);
	    }

	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}

	@DeleteMapping("/{productID}")
	public ResponseEntity<ResponseObject> remove(ModelMap model, @PathVariable("productID") int productID) {
		Optional<Product> opt = productService.findById(productID);
		String message = "";

		if (opt.isPresent()) {
			Product result = opt.get();
			try {
				productService.deleteById(productID);
				message = "Xoa thanh cong!";
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(null, "OK", message, null));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				message = e.toString();
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new ResponseObject(null, "FAILURE", message, null));
			}

		}

		message = "Khong tim thay video";
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(null, "NOT FOUND", message, null));
	}

	@GetMapping("search")
	public ResponseEntity<List<Product>> searchView(
			@RequestParam(name = "searchValue", required = false) String searchValue) {

		List<Product> result = null;

		if (StringUtils.hasText(searchValue)) {
			result = productService.findByProductNameContaining(searchValue);
		} else {
			result = productService.findAll();
		}

		return ResponseEntity.status(HttpStatus.OK).body(result);
	}

	@GetMapping("/images/{fileName:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serverFile(@PathVariable String fileName) {

		Resource resource = storageService.loadAsResource(fileName);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

//	@GetMapping("searchPagination")
//	public ResponseEntity<ResponseObject> searchAndPaginating(@RequestParam Optional<String> searchValue,
//			@RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size,
//			@RequestParam Optional<String> sortDirector, @RequestParam Optional<Integer> categoryID,
//			@RequestParam Optional<Integer> startAmount, @RequestParam Optional<Integer> endAmount,
//			@RequestParam Optional<Boolean> isActive) {
//
//		long count = productService.count();
//		int currentPage = page.orElse(0);
//		int sizeEachPage = size.orElse(5); 
//
//		String sortDirection = sortDirector.orElse("asc");
//		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by("title").ascending()
//				: Sort.by("title").descending();
//
//		Pageable pageable = PageRequest.of(currentPage, sizeEachPage, sort);
//
//		Page<Product> result;
//
//		if (searchValue.isPresent() && StringUtils.hasText(searchValue.get())) {
//			result = productService.findByProductNameContaining(searchValue.get(), pageable);
//		} else if (categoryID.isPresent() && !categoryID.isEmpty()) {
//
//			Optional<Category> cate = categoryService.findById(categoryID.get());
//
//			result = productService.findByCategory(cate.get(), pageable);
//		} else if (startAmount.isPresent() && !startAmount.isEmpty() && endAmount.isPresent() && !endAmount.isEmpty()) {
//			result = productService.findByUnitPriceBetween(startAmount.get(), endAmount.get(), pageable);
//
//		} else if (!isActive.isEmpty() && isActive.isPresent()) {
//			result = productService.findByActive(isActive.get(), pageable);
//		} else {
//			result = productService.findAll(pageable);
//		}
//
//		int totalPages = result.getTotalPages();
//
//		List<Integer> pageNumbers = null;
//		if (totalPages > 0) {
//			int start = Math.max(0, currentPage - 2);
//			int end = Math.min(currentPage + 2, totalPages - 1);
//
//			pageNumbers = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
//		}
//
//		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "List video", result));
//	}
}
