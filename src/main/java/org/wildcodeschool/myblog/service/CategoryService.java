package org.wildcodeschool.myblog.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.wildcodeschool.myblog.dto.CategoryDto;
import org.wildcodeschool.myblog.exception.RessourceNotFoundException;
import org.wildcodeschool.myblog.model.Category;
import org.wildcodeschool.myblog.repository.CategoryRepository;

@Service
public class CategoryService {

	
	private final CategoryRepository categoryRepository;
	
	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}
	
	public List<CategoryDto> getAllCategories() {
		return categoryRepository.findAll().stream().map(category -> CategoryDto.convertToDto(category)).toList();
	}
	
	public CategoryDto getCategoryById(Long id) {
		return categoryRepository.findById(id).map(CategoryDto::convertToDto).orElseThrow(() -> new RessourceNotFoundException("La categorie avec l'id " + id + " n'a pas été trouvé"));
	}
	
	public CategoryDto createCategory(Category category) {
		category.setCreatedAt(LocalDateTime.now());
		category.setUpdatedAt(LocalDateTime.now());
		
		Category savedCategory =  categoryRepository.save(category);
		return CategoryDto.convertToDto(savedCategory);
	}
	
	public CategoryDto updateCategory(Long id, Category categoryDetails) {
		Category category = categoryRepository.findById(id)
				.orElseThrow(() -> new RessourceNotFoundException("La categorie avec l'id " + id + " n'a pas été trouvé"));
	
		category.setUpdatedAt(LocalDateTime.now());
		category.setName(categoryDetails.getName());
		
		Category updatedCategory = categoryRepository.save(category);
		
		return CategoryDto.convertToDto(updatedCategory);
	}
	
	public boolean deleteCategory(Long id) {
		Category category = categoryRepository.findById(id)
				.orElseThrow(() -> new RessourceNotFoundException("La categorie avec l'id " + id + " n'a pas été trouvé"));
		categoryRepository.delete(category);
		return true;
		
	}
}
