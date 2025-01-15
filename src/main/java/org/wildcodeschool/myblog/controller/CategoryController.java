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
import org.wildcodeschool.myblog.dto.ArticleDto;
import org.wildcodeschool.myblog.model.Article;
import org.wildcodeschool.myblog.model.Category;
import org.wildcodeschool.myblog.repository.CategoryRepository;

@RestController
@RequestMapping("/category")
public class CategoryController {

	private final CategoryRepository categoryRepository;
	
	
	public CategoryController(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}
	
	@GetMapping
	public ResponseEntity<List<CategoryDto>> getAllCAtegories(){
		List<Category> categories = categoryRepository.findAll();
		
		if(categories.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		
		List<CategoryDto> categoryDto = categories.stream().map(this::convertToDTO).collect(Collectors.toList());
		
		return ResponseEntity.ok(categoryDto);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id){
		Category category = categoryRepository.findById(id).orElse(null);
		if(category == null){
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(convertToDTO(category));
	}
	
	@PostMapping
	public ResponseEntity<CategoryDto> addCategory(@RequestBody Category category){
		category.setCreatedAt(LocalDateTime.now());
		category.setUpdatedAt(LocalDateTime.now());
		
		Category savedCategory =  categoryRepository.save(category);
		return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(savedCategory));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id, @RequestBody Category categoryDetails){
		Category category = categoryRepository.findById(id).orElse(null);
		if(category == null) {
			return ResponseEntity.notFound().build();
		}
		category.setUpdatedAt(LocalDateTime.now());
		category.setName(categoryDetails.getName());
		
		Category updatedCategory = categoryRepository.save(category);
		
		return ResponseEntity.ok(convertToDTO(updatedCategory));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCategory(@PathVariable Long id){
		Category category = categoryRepository.findById(id).orElse(null);
		if(category == null) {
			return ResponseEntity.notFound().build();
		}
		categoryRepository.delete(category);
		return ResponseEntity.noContent().build();
	}
	
	private CategoryDto convertToDTO(Category category){
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setId(category.getId());
		categoryDto.setName(category.getName());
		
		if(category.getArticles() != null) {
			categoryDto.setArticles(category.getArticles().stream().map(article -> ArticleDto.convertToDTO(article)).collect(Collectors.toList()));
		}
		
		return categoryDto;
	}
	
	
}
