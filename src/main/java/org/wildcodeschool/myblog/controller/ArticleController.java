package org.wildcodeschool.myblog.controller;

import java.time.LocalDateTime;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.wildcodeschool.myblog.model.Article;
import org.wildcodeschool.myblog.repository.ArticleRepository;

@RestController
@RequestMapping("/articles")
public class ArticleController {

	private final ArticleRepository articleRepository;
	
	public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }
	
	public ResponseEntity<List<Article>> getAllArticle(){
		List<Article> articles = articleRepository.findAll();
		if(articles.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(articles);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Article> getArticleById(@PathVariable Long id){
		Article article = articleRepository.findById(id).orElse(null);
		if(article == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(article);
	}
	
	@PostMapping
	public ResponseEntity<Article> createArticle(@RequestBody Article article){
		article.setCreatedAt(LocalDateTime.now());
		article.setUpdatedAt(LocalDateTime.now());
		Article savedArticle = articleRepository.save(article);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Article> updateArticle(@PathVariable Long id,@RequestBody Article articleDetails){
		Article article = articleRepository.findById(id).orElse(null);
		if(article == null) {
			return ResponseEntity.notFound().build();
		}
		article.setTitle(articleDetails.getTitle());
		article.setContent(articleDetails.getContent());
		article.setUpdatedAt(LocalDateTime.now());
		
		Article updatedArticle = articleRepository.save(article);
		return ResponseEntity.ok(updatedArticle);
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
	public ResponseEntity<List<Article>> getArticlesByContent(@RequestParam String search){
		List<Article> articles = articleRepository.findByArticleContaining(search);
		if(articles.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(articles);
	}
	
	@GetMapping("/articleByDate")
	public ResponseEntity<List<Article>> getArticlesCreateAfter(@RequestParam Date creationDate){
		List<Article> articles = articleRepository.findByCreatedAtAfter(creationDate);
		if(articles.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(articles);
		}
	
	@GetMapping("/lastFive")
	public ResponseEntity<List<Article>> getFiveLastArticles(){
		List<Article> articles = articleRepository.findTop5ByOrderByCreatedAtDesc();
		if(articles.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(articles);
	}
}
