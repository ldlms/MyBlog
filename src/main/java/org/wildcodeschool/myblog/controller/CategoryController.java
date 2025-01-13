package org.wildcodeschool.myblog.controller;

import java.time.LocalDateTime;
import java.util.List;

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
	public ResponseEntity<List<Category>> getAllCAtegories(){
		List<Category> categories = categoryRepository.findAll();
		if(categories.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(categories);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Category> getCategoryById(@PathVariable Long id){
		Category category = categoryRepository.findById(id).orElse(null);
		if(category == null){
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(category);
	}
	
	@PostMapping
	public ResponseEntity<Category> addCategory(@RequestBody Category category){
		category.setCreatedAt(LocalDateTime.now());
		category.setUpdatedAt(LocalDateTime.now());
		
		Category savedCategory =  categoryRepository.save(category);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category categoryDetails){
		Category category = categoryRepository.findById(id).orElse(null);
		if(category == null) {
			return ResponseEntity.notFound().build();
		}
		category.setUpdatedAt(LocalDateTime.now());
		category.setName(categoryDetails.getName());
		
		Category updatedCategory = categoryRepository.save(category);
		
		return ResponseEntity.ok(updatedCategory);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Category> deleteCategory(@PathVariable Long id){
		Category category = categoryRepository.findById(id).orElse(null);
		if(category == null) {
			return ResponseEntity.notFound().build();
		}
		categoryRepository.delete(category);
		return ResponseEntity.noContent().build();
	}
	
	
}
