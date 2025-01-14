package org.wildcodeschool.myblog.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.wildcodeschool.myblog.dto.ArticleDto;
import org.wildcodeschool.myblog.model.Article;
import org.wildcodeschool.myblog.model.Category;
import org.wildcodeschool.myblog.repository.ArticleRepository;
import org.wildcodeschool.myblog.repository.CategoryRepository;

@RestController
@RequestMapping("/articles")
public class ArticleController {

	private final ArticleRepository articleRepository;
	private final CategoryRepository categoryRepository;
	
	public ArticleController(ArticleRepository articleRepository, CategoryRepository categoryRepository) {
        this.articleRepository = articleRepository;
        this.categoryRepository = categoryRepository;
    }
	
	@GetMapping
	public ResponseEntity<List<ArticleDto>> getAllArticle(){
		
	    List<ArticleDto> articlesDto = articleRepository.findAll()
	        .stream()
	        .map(this::convertToDTO)  
	        .collect(Collectors.toList());
	    
	    if(articlesDto.isEmpty()) {  
	        return ResponseEntity.noContent().build();
	    }
	        
	    return ResponseEntity.ok(articlesDto);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ArticleDto> getArticleById(@PathVariable Long id){
		Article article = articleRepository.findById(id).orElse(null);
		if(article == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(convertToDTO(article));
	}
	
	@PostMapping
	public ResponseEntity<ArticleDto> createArticle(@RequestBody Article article){
		article.setCreatedAt(LocalDateTime.now());
		article.setUpdatedAt(LocalDateTime.now());
		
		if(article.getCategory() != null) {
			Category category = categoryRepository.findById(article.getCategory().getId()).orElse(null);
			if(category == null) {
				return ResponseEntity.badRequest().body(null);
			}
			article.setCategory(category);
		}
		
		Article savedArticle = articleRepository.save(article);
		return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(savedArticle));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ArticleDto> updateArticle(@PathVariable Long id,@RequestBody Article articleDetails){
		Article article = articleRepository.findById(id).orElse(null);
		if(article == null) {
			return ResponseEntity.notFound().build();
		}
		article.setTitle(articleDetails.getTitle());
		article.setContent(articleDetails.getContent());
		article.setUpdatedAt(LocalDateTime.now());
		
		if(articleDetails.getCategory() != null) {
			Category category = categoryRepository.findById(articleDetails.getCategory().getId()).orElse(null);
			if(category == null) {
				return ResponseEntity.badRequest().body(null);
			}
			article.setCategory(category);
		}
		
		Article updatedArticle = articleRepository.save(article);
		return ResponseEntity.ok(convertToDTO(updatedArticle));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Article> deleteArticle(@PathVariable Long id){
		Article article = articleRepository.findById(id).orElse(null);
		if(article == null) {
			return ResponseEntity.notFound().build();
		} 
		articleRepository.delete(article);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/search-content")
	public ResponseEntity<List<ArticleDto>> getArticlesByContent(@RequestParam String search){
		List<ArticleDto> articles = articleRepository.findBycontentContaining(search)
				.stream()
				.map(this::convertToDTO)
				.collect(Collectors.toList());
		if(articles.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(articles);
	}
	
	@GetMapping("/articleByDate")
	public ResponseEntity<List<ArticleDto>> getArticlesCreateAfter(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime creationDate){
	    
		List<ArticleDto> articles = articleRepository.findByCreatedAtAfter(creationDate)
													.stream()
													.map(this::convertToDTO)
													.collect(Collectors.toList());
		if(articles.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(articles);
		}
	
	@GetMapping("/lastFive")
	public ResponseEntity<List<ArticleDto>> getFiveLastArticles(){
		List<ArticleDto> articles = articleRepository.findTop5ByOrderByCreatedAtDesc()
													.stream()
													.map(this::convertToDTO)
													.collect(Collectors.toList());
		if(articles.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(articles);
	}
	
	private ArticleDto convertToDTO(Article article) {
	    ArticleDto articleDTO = new ArticleDto();
	    articleDTO.setId(article.getId());
	    articleDTO.setTitle(article.getTitle());
	    articleDTO.setContent(article.getContent());
	    articleDTO.setUpdatedAt(article.getUpdatedAt());
	    if (article.getCategory() != null) {
	        articleDTO.setCategoryName(article.getCategory().getName());
	    }
	    return articleDTO;
	}
}
