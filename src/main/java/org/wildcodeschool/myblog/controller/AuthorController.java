package org.wildcodeschool.myblog.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import org.wildcodeschool.myblog.dto.CreateAuthorDto;
import org.wildcodeschool.myblog.model.Article;
import org.wildcodeschool.myblog.model.ArticleAuthor;
import org.wildcodeschool.myblog.model.Author;
import org.wildcodeschool.myblog.repository.ArticleAuthorRepository;
import org.wildcodeschool.myblog.repository.ArticleRepository;
import org.wildcodeschool.myblog.repository.AuthorRepository;
import org.wildcodeschool.myblog.service.AuthorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/authors")
public class AuthorController {
	
	private final AuthorService authorService;
	
	public AuthorController(AuthorService authorService) {
		this.authorService = authorService;
	}
	
	@GetMapping
	public ResponseEntity<List<AuthorDto>> getAllAuthors(){
		List<AuthorDto> authors = authorService.getAllAuthors();
		if(authors.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(authors);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<AuthorDto> getAuthorById(@PathVariable Long id){
		AuthorDto author = authorService.getAuthorById(id);	
		return ResponseEntity.ok(author);
	}
	
	@PostMapping
	public ResponseEntity<AuthorDto> createAuthor(@Valid @RequestBody CreateAuthorDto author){
		AuthorDto authorCreated = authorService.createAuthor(author);
		return ResponseEntity.ok(authorCreated);
	}

	@PreAuthorize("#id == authentication.principal.id or hasRole('ROLE_ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<AuthorDto> updateAuthor(@Valid @RequestBody CreateAuthorDto authorDetails, @PathVariable Long id){
		AuthorDto updatedAuthor = authorService.updateAuthor(authorDetails, id);
		return ResponseEntity.ok(updatedAuthor);
		
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> DeleteAuthor(@PathVariable Long id){
		if (authorService.deleteAuthor(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
	}
}
