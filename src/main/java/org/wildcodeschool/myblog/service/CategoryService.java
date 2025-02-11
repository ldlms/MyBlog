package org.wildcodeschool.myblog.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.wildcodeschool.myblog.dto.CategoryDto;
import org.wildcodeschool.myblog.dto.CreateCategoryDto;
import org.wildcodeschool.myblog.exception.RessourceNotFoundException;
import org.wildcodeschool.myblog.model.Article;
import org.wildcodeschool.myblog.model.Category;
import org.wildcodeschool.myblog.repository.ArticleRepository;
import org.wildcodeschool.myblog.repository.CategoryRepository;

@Service
public class CategoryService {

	
	private final CategoryRepository categoryRepository;
	private final ArticleRepository articleRepository;
	
	public CategoryService(CategoryRepository categoryRepository, ArticleRepository articleRepository) {
		this.categoryRepository = categoryRepository;
		this.articleRepository = articleRepository;
	}
	
	public List<CategoryDto> getAllCategories() {
		return categoryRepository.findAll().stream().map(category -> CategoryDto.convertToDto(category)).toList();
	}
	
	public CategoryDto getCategoryById(Long id) {
		return categoryRepository.findById(id).map(CategoryDto::convertToDto).orElseThrow(() -> new RessourceNotFoundException("La categorie avec l'id " + id + " n'a pas été trouvé"));
	}
	
	public CategoryDto createCategory(CreateCategoryDto categoryDto) {
		Category category = CreateCategoryDto.convertToEntity(categoryDto);
		
		/*if(!categoryDto.articleId().isEmpty()) {
			List<Article> categoryArticles = new ArrayList<Article>();
			for(Long articleId : categoryDto.articleId()) {
				Article article = articleRepository.findById(articleId).orElse(null);
				if(category != null) {
					categoryArticles.add(article);
				}
			}
			category.setArticles(categoryArticles);
		}*/
		
		Category savedCategory =  categoryRepository.save(category);
		return CategoryDto.convertToDto(savedCategory);
	}
	
	public CategoryDto updateCategory(Long id, CreateCategoryDto categoryDetails) {
		Category category = categoryRepository.findById(id)
				.orElseThrow(() -> new RessourceNotFoundException("La categorie avec l'id " + id + " n'a pas été trouvé"));
	
		category.setUpdatedAt(LocalDateTime.now());
		category.setName(categoryDetails.name());
		
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
