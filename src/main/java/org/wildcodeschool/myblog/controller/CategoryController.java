package org.wildcodeschool.myblog.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wildcodeschool.myblog.dto.CategoryDto;
import org.wildcodeschool.myblog.dto.CreateCategoryDto;
import org.wildcodeschool.myblog.dto.ArticleDto;
import org.wildcodeschool.myblog.model.Article;
import org.wildcodeschool.myblog.model.Category;
import org.wildcodeschool.myblog.repository.CategoryRepository;
import org.wildcodeschool.myblog.service.CategoryService;

@RestController
@RequestMapping("/category")
public class CategoryController {

	private final CategoryService categoryService;
	
	
	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}
	
	@GetMapping
	public ResponseEntity<List<CategoryDto>> getAllCAtegories(){
		List<CategoryDto> categories = categoryService.getAllCategories();
		
		if (categories.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(categories);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id){
		CategoryDto category = categoryService.getCategoryById(id);
		return ResponseEntity.ok(category);
	}
	
	@PostMapping
	public ResponseEntity<CategoryDto> addCategory(@RequestBody CreateCategoryDto category){
		CategoryDto categoryDto = categoryService.createCategory(category);
		
		return ResponseEntity.ok(categoryDto);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id, @RequestBody CreateCategoryDto categoryDetails){
		CategoryDto savedCategory = categoryService.updateCategory(id, categoryDetails);
		return ResponseEntity.ok(savedCategory);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCategory(@PathVariable Long id){
		categoryService.deleteCategory(id);
		return ResponseEntity.noContent().build();
	}
	
	
}
