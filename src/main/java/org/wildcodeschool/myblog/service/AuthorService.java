package org.wildcodeschool.myblog.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.wildcodeschool.myblog.dto.ArticleContributionDto;
import org.wildcodeschool.myblog.dto.ArticleDto;
import org.wildcodeschool.myblog.dto.AuthorDto;
import org.wildcodeschool.myblog.dto.CreateAuthorDto;
import org.wildcodeschool.myblog.exception.RessourceNotFoundException;
import org.wildcodeschool.myblog.model.Article;
import org.wildcodeschool.myblog.model.ArticleAuthor;
import org.wildcodeschool.myblog.model.Author;
import org.wildcodeschool.myblog.repository.ArticleAuthorRepository;
import org.wildcodeschool.myblog.repository.ArticleRepository;
import org.wildcodeschool.myblog.repository.AuthorRepository;
import org.wildcodeschool.myblog.repository.CategoryRepository;
import org.wildcodeschool.myblog.repository.ImageRepository;

@Service
public class AuthorService {
	
	private final AuthorRepository authorRepository;
	private final ArticleRepository articleRepository;
	private final ArticleAuthorRepository articleAuthorRepository;
	
	public AuthorService(AuthorRepository authorRepository, ArticleRepository articleRepository, ArticleAuthorRepository articleAuthorRepository){
		this.authorRepository = authorRepository;
		this.articleAuthorRepository = articleAuthorRepository;
		this.articleRepository = articleRepository;
	} 
	
	
	public List<AuthorDto> getAllAuthors(){
		return authorRepository.findAll().stream().map(author -> AuthorDto.convertToDTO(author)).toList();
	}
	
	public AuthorDto getAuthorById(Long id) {
		return authorRepository.findById(id)
				.map(AuthorDto::convertToDTO)
				.orElseThrow(() -> new RessourceNotFoundException("L'autheur avec l'id " + id + " n'a pas été trouvé"));
	}
		
	public AuthorDto createAuthor(CreateAuthorDto authorDto) {
		
		Author author = CreateAuthorDto.convertToEntity(authorDto);
		
		Author savedAuthor = authorRepository.save(author);
		

		for(ArticleContributionDto articleAuthorDto : authorDto.article()) {
			Article article = articleRepository.findById(articleAuthorDto.articleId())
					.orElseThrow(() -> new RessourceNotFoundException("L'article avec le titre " + articleAuthorDto.articleId() + " n'a pas été trouvé"));
			
			ArticleAuthor articleAuthor = new ArticleAuthor();
			articleAuthor.setArticle(article);
			articleAuthor.setAuthor(author);
			articleAuthor.setContribution(articleAuthorDto.contribution());
			articleAuthorRepository.save(articleAuthor);
		}
		
		return AuthorDto.convertToDTO(savedAuthor);
	}
	
	public AuthorDto updateAuthor(CreateAuthorDto authorDetails, Long id) {


		Author author = authorRepository.findById(id)
				.orElseThrow(() -> new RessourceNotFoundException("L'autheur avec l'id " + id + " n'a pas été trouvé"));
		
		author.setUpdatedAt(LocalDateTime.now());
		author.setFirstname(authorDetails.firstname());
		author.setLastname(authorDetails.lastname());
				
			for (ArticleAuthor oldArticleAuthor : author.getArticleAuthors()) {
                articleAuthorRepository.delete(oldArticleAuthor);
            }
			
			List<ArticleAuthor> ValidArticleAuthor = new ArrayList<ArticleAuthor>();
			
			for(ArticleContributionDto articleAuthor : authorDetails.article()) {
				Article article = articleRepository.findById(articleAuthor.articleId())
						.orElseThrow(() -> new RessourceNotFoundException("L'article avec le titre " + articleAuthor.articleId() + " n'a pas été trouvé"));
				
				
				ArticleAuthor newArticleAuthor = new ArticleAuthor();
				newArticleAuthor.setArticle(article);
				newArticleAuthor.setAuthor(author);
				newArticleAuthor.setContribution(articleAuthor.contribution());
				
				ValidArticleAuthor.add(newArticleAuthor);
			}
			for(ArticleAuthor validAa : ValidArticleAuthor) {
				articleAuthorRepository.save(validAa);
			}
			author.setArticleAuthors(ValidArticleAuthor);
		
		Author savedAuthor = authorRepository.save(author);
		
		return AuthorDto.convertToDTO(savedAuthor);
	}
	
	public boolean deleteAuthor(Long id) {
		Author author = authorRepository.findById(id)
				.orElseThrow(() -> new RessourceNotFoundException("L'autheur avec l'id " + id + " n'a pas été trouvé"));
		
		articleAuthorRepository.deleteAll(author.getArticleAuthors());
		authorRepository.delete(author);
		return true;
	}
}
