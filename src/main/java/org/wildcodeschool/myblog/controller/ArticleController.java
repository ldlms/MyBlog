package org.wildcodeschool.myblog.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
import org.wildcodeschool.myblog.dto.CreateArticleDto;
import org.wildcodeschool.myblog.model.Article;
import org.wildcodeschool.myblog.model.ArticleAuthor;
import org.wildcodeschool.myblog.model.Author;
import org.wildcodeschool.myblog.model.Category;
import org.wildcodeschool.myblog.model.Image;
import org.wildcodeschool.myblog.repository.ArticleAuthorRepository;
import org.wildcodeschool.myblog.repository.ArticleRepository;
import org.wildcodeschool.myblog.repository.AuthorRepository;
import org.wildcodeschool.myblog.repository.CategoryRepository;
import org.wildcodeschool.myblog.repository.ImageRepository;
import org.wildcodeschool.myblog.service.ArticleService;

@RestController
@RequestMapping("/articles")
public class ArticleController {

	private final ArticleRepository articleRepository;
	private final ArticleService articleService;

	public ArticleController(ArticleRepository articleRepository, CategoryRepository categoryRepository,
			ImageRepository imageRepository, ArticleAuthorRepository articleAuthorRepository, AuthorRepository authorRepository, ArticleService articleService) {
		this.articleRepository = articleRepository;
		this.articleService = articleService;
	}

	@GetMapping
	public ResponseEntity<List<ArticleDto>> getAllArticle() {

		List<ArticleDto> articlesDto = articleService.getAllArticles();
		if (articlesDto.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(articlesDto);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ArticleDto> getArticleById(@PathVariable Long id) {
		ArticleDto article = articleService.getArticleById(id);
		return ResponseEntity.ok(article);
	}

	@PostMapping
	public ResponseEntity<ArticleDto> createArticle(@RequestBody CreateArticleDto articleDto) {
		ArticleDto createdArticle = articleService.createArticle(articleDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdArticle);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ArticleDto> updateArticle(@PathVariable Long id, @RequestBody CreateArticleDto articleDetails) {
		ArticleDto articleUpdated = articleService.updateArticle(id, articleDetails);
	return ResponseEntity.ok(articleUpdated);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteArticle(@PathVariable Long id){
		if (articleService.deleteArticle(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
	}

	@GetMapping("/search-content")
	public ResponseEntity<List<ArticleDto>> getArticlesByContent(@RequestParam String search) {
		List<ArticleDto> articles = articleRepository.findBycontentContaining(search).stream().map(article -> ArticleDto.convertToDTO(article))
				.collect(Collectors.toList());
		if (articles.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(articles);
	}

	@GetMapping("/articleByDate")
	public ResponseEntity<List<ArticleDto>> getArticlesCreateAfter(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime creationDate) {

		List<ArticleDto> articles = articleRepository.findByCreatedAtAfter(creationDate).stream()
				.map(article -> ArticleDto.convertToDTO(article)).collect(Collectors.toList());
		if (articles.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(articles);
	}

	@GetMapping("/lastFive")
	public ResponseEntity<List<ArticleDto>> getFiveLastArticles() {
		List<ArticleDto> articles = articleRepository.findTop5ByOrderByCreatedAtDesc().stream().map(article -> ArticleDto.convertToDTO(article))
				.collect(Collectors.toList());
		if (articles.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(articles);
	}

}
