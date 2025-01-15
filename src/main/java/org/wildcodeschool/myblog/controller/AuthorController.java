package org.wildcodeschool.myblog.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wildcodeschool.myblog.dto.ArticleDto;
import org.wildcodeschool.myblog.dto.AuthorDto;
import org.wildcodeschool.myblog.model.Article;
import org.wildcodeschool.myblog.model.ArticleAuthor;
import org.wildcodeschool.myblog.model.Author;
import org.wildcodeschool.myblog.repository.ArticleAuthorRepository;
import org.wildcodeschool.myblog.repository.ArticleRepository;
import org.wildcodeschool.myblog.repository.AuthorRepository;

@RestController
@RequestMapping("/authors")
public class AuthorController {
	
	private final AuthorRepository authorRepository;
	private final ArticleAuthorRepository articleAuthorRepository;
	private final ArticleRepository articleRepository;
	
	public AuthorController(AuthorRepository authorRepository,ArticleAuthorRepository articleAuthorRepository, ArticleRepository articleRepository) {
		this.authorRepository = authorRepository;
		this.articleAuthorRepository = articleAuthorRepository;
		this.articleRepository = articleRepository;
	}
	
	@GetMapping
	public ResponseEntity<List<AuthorDto>> getAllAuthors(){
		List<AuthorDto> authors = authorRepository.findAll().stream().map(author -> AuthorDto.convertToDTO(author)).toList();
		if(authors.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(authors);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<AuthorDto> getAuthorById(@PathVariable Long id){
		Author author = authorRepository.findById(id).orElse(null);
		
		if(author == null) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(AuthorDto.convertToDTO(author));
	}
	
	@PostMapping
	public ResponseEntity<AuthorDto> createAuthor(@RequestBody Author author){
		author.setCreatedAt(LocalDateTime.now());
		author.setUpdatedAt(LocalDateTime.now());
		
		Author savedAuthor = authorRepository.save(author);
		
		if(author.getArticleAuthors() != null && !author.getArticleAuthors().isEmpty()) {
		for(ArticleAuthor articleAuthor : author.getArticleAuthors()) {
			Article article = articleAuthor.getArticle();
			article = articleRepository.findFirstByTitle(article.getTitle()).orElse(null);
			if (article == null) {
                return ResponseEntity.badRequest().body(null);
            }
			articleAuthor.setArticle(article);
			articleAuthor.setAuthor(author);
			articleAuthor.setContribution(articleAuthor.getContribution());
			articleAuthorRepository.save(articleAuthor);
		}
		}
		return ResponseEntity.ok(AuthorDto.convertToDTO(savedAuthor));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<AuthorDto> updateAuthor(@RequestBody Author authorDetails, @PathVariable Long id){
		Author author = authorRepository.findById(id).orElse(null);
		if(author == null) {
			return ResponseEntity.notFound().build();
		}
		author.setUpdatedAt(LocalDateTime.now());
		author.setFirstname(authorDetails.getFirstname());
		author.setLastname(authorDetails.getLastname());
		
		if(authorDetails.getArticleAuthors() != null && !authorDetails.getArticleAuthors().isEmpty()) {
			
			for (ArticleAuthor oldArticleAuthor : author.getArticleAuthors()) {
                articleAuthorRepository.delete(oldArticleAuthor);
            }
			
			List<ArticleAuthor> ValidArticleAuthor = new ArrayList<ArticleAuthor>();
			
			for(ArticleAuthor articleAuthor : authorDetails.getArticleAuthors()) {
				Article article = articleAuthor.getArticle();
				article = articleRepository.findFirstByTitle(article.getTitle()).orElse(null);
				if(article == null) {
					return ResponseEntity.badRequest().body(null);
				}
				
				ArticleAuthor newArticleAuthor = new ArticleAuthor();
				newArticleAuthor.setArticle(article);
				newArticleAuthor.setAuthor(author);
				newArticleAuthor.setContribution(articleAuthor.getContribution());
				
				ValidArticleAuthor.add(newArticleAuthor);
			}
			for(ArticleAuthor validAa : ValidArticleAuthor) {
				articleAuthorRepository.save(validAa);
			}
			author.setArticleAuthors(ValidArticleAuthor);
		}
		Author savedAuthor = authorRepository.save(author);
		return ResponseEntity.ok(AuthorDto.convertToDTO(savedAuthor));
		
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> DeleteAuthor(@PathVariable Long id){
		Author author = authorRepository.findById(id).orElse(null);
		if(author == null) {
			return ResponseEntity.badRequest().body(null);
		}
		if(!author.getArticleAuthors().isEmpty()) {
			for(ArticleAuthor Aa : author.getArticleAuthors()) {
				articleAuthorRepository.delete(Aa);
			}
		}
		authorRepository.delete(author);
		return ResponseEntity.noContent().build();
	}
}
