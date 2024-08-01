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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.rubikShopApi.entity.Category;
import com.example.rubikShopApi.entity.Product;
import com.example.rubikShopApi.request.CategoryModel;
import com.example.rubikShopApi.request.ResponseObject;
import com.example.rubikShopApi.service.ICategoryService;
import com.example.rubikShopApi.service.IStorageService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("admin/categories")
public class CategoryControllerForAdmin {

	@Autowired
	ICategoryService categoryService;

	@Autowired
	IStorageService storageService;

	@Autowired
	Cloudinary cloudinary;

	@GetMapping("")
	public List<Category> listCategories() {
		List<Category> list = categoryService.findAll();
		return list;
	}

	@PostMapping("/save")
	public ResponseEntity<Category> save(@ModelAttribute CategoryModel cate, BindingResult result) {

		if (result.hasErrors()) {
//			return ResponseEntity.badRequest().body(null);
			System.out.println(result.toString());
		}

		Category entity = new Category();

//		coppy model sang entity
		BeanUtils.copyProperties(cate, entity);

		if (!cate.getImageFile().isEmpty()) {
			if (cate.getIsResource() == true) {
				UUID uuID = UUID.randomUUID();
				String uuIDString = uuID.toString();
				entity.setCategoryImage(storageService.getStorageFileName(cate.getImageFile(), uuIDString));
				storageService.store(cate.getImageFile(), entity.getCategoryImage());
			} else {
				try {

					Map cloudinaryResult = cloudinary.uploader().upload(cate.getImageFile().getBytes(),
							ObjectUtils.asMap("resource_type", "auto"));

					String imageUrl = (String) cloudinaryResult.get("secure_url");

					entity.setCategoryImage(imageUrl);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		categoryService.save(entity);

		return ResponseEntity.ok().body(entity);
	}

	@GetMapping("/{categoryID}")
	public Category getCategory(@PathVariable("categoryID") int categoryID) {

		Optional<Category> result = categoryService.findById(categoryID);

		return result.get();

	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<Category> update(@PathVariable("id") int id, @ModelAttribute CategoryModel resource) {
		
		System.out.println(resource.toString());

	    Optional<Category> opt = categoryService.findById(id);

	    if (opt.isPresent()) {
	        Category cateFromModel = new Category();
	        Category currentSearch = opt.get();

	        BeanUtils.copyProperties(resource, cateFromModel);

	        if (resource.getImageFile() != null && !resource.getImageFile().isEmpty()) {
	            if (resource.getIsResource()) {
	                UUID uuID = UUID.randomUUID();
	                String uuIDString = uuID.toString();
	                cateFromModel.setCategoryImage(storageService.getStorageFileName(resource.getImageFile(), uuIDString));
	                storageService.store(resource.getImageFile(), cateFromModel.getCategoryImage());
	            } else {
	                try {
	                    Map cloudinaryResult = cloudinary.uploader().upload(resource.getImageFile().getBytes(),
	                            ObjectUtils.asMap("resource_type", "auto"));
	                    String imageUrl = (String) cloudinaryResult.get("secure_url");
	                    cateFromModel.setCategoryImage(imageUrl);
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        } else {
	            cateFromModel.setCategoryImage(currentSearch.getCategoryImage());
	        }
	        
	        System.out.println(cateFromModel.toString());

	        categoryService.save(cateFromModel);
	        return ResponseEntity.ok().body(cateFromModel);
	    }

	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}


	@DeleteMapping("/{categoryID}")
	public ResponseEntity<String> delete(@PathVariable("categoryID") int categoryID) {

		Optional<Category> opt = categoryService.findById(categoryID);

		if (opt.isPresent()) {
			if (opt.get().getProducts().size() <= 0) {
				categoryService.deleteById(categoryID);
				return ResponseEntity.ok().body("Delete Successfully");
			}
			return ResponseEntity.badRequest().body("Exist products in this Category!");
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
	}

	@GetMapping("searchPagination")
	public ResponseEntity<ResponseObject> searchAndPaginating(
			@RequestParam(defaultValue = "") String searchValue, @RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> size, @RequestParam Optional<String> sortDirector) {

		long count = categoryService.count();
		int currentPage = page.orElse(0);
		int sizeEachPage = size.orElse(9);

		String sortDirection = sortDirector.orElse("asc");
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by("categoryName").ascending()
				: Sort.by("categoryName").descending();

		Pageable pageable = PageRequest.of(currentPage, sizeEachPage, sort);

		Page<Category> result;

		if (!searchValue.isBlank() && StringUtils.hasText(searchValue)) {
			System.out.println("Search Value: " + searchValue);
			result = categoryService.findByCategoryNameContaining(searchValue, pageable);
		} else {
			result = categoryService.findAll(pageable);
		}

//		int totalPages = result.getTotalPages();

//		List<Integer> pageNumbers = null;
//		if (totalPages > 0) {
//			int start = Math.max(0, currentPage - 2);
//			int end = Math.min(currentPage + 2, totalPages - 1);
//
//			pageNumbers = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
//		}

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(null,"OK", "List video", result));
	}

	@GetMapping("/images/{fileName:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serverFile(@PathVariable String fileName) {

		Resource resource = storageService.loadAsResource(fileName);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

}
