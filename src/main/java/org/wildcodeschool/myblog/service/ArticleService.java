package org.wildcodeschool.myblog.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.wildcodeschool.myblog.dto.ArticleDto;
import org.wildcodeschool.myblog.dto.AuthorContributionDto;
import org.wildcodeschool.myblog.dto.CreateArticleDto;
import org.wildcodeschool.myblog.dto.CreateImageDto;
import org.wildcodeschool.myblog.dto.ImageDto;
import org.wildcodeschool.myblog.exception.RessourceNotFoundException;
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

@Service
public class ArticleService {

	private final ArticleRepository articleRepository;
	private final CategoryRepository categoryRepository;
	private final ImageRepository imageRepository;
	private final AuthorRepository authorRepository;
	private final ArticleAuthorRepository articleAuthorRepository;

	public ArticleService(ArticleRepository articleRepository, CategoryRepository categoryRepository,
			ImageRepository imageRepository, AuthorRepository authorRepository,
			ArticleAuthorRepository articleAuthorRepository) {
		this.articleRepository = articleRepository;
		this.categoryRepository = categoryRepository;
		this.imageRepository = imageRepository;
		this.authorRepository = authorRepository;
		this.articleAuthorRepository = articleAuthorRepository;
	}

	public List<ArticleDto> getAllArticles() {
		List<Article> articles = articleRepository.findAll();
		return articles.stream().map(ArticleDto::convertToDTO).toList();
	}

	public ArticleDto getArticleById(Long id) {
		return articleRepository.findById(id).map(ArticleDto::convertToDTO)
				.orElseThrow(() -> new RessourceNotFoundException("L'article avec l'id " + id + " n'a pas été trouvé"));
	}

	public ArticleDto createArticle(CreateArticleDto articleDto) { 
		
		Article article = CreateArticleDto.convertToEntity(articleDto);
	
			Category category = categoryRepository.findById(articleDto.categoryId())
					.orElseThrow(() -> new RessourceNotFoundException(
							"La category avec l'id " + articleDto.categoryId() + " n'a pas été trouvé"));
			article.setCategory(category);

			List<Image> validImages = new ArrayList<>();
			for (CreateImageDto imageDto : articleDto.images()) {
				if (imageDto.id() != null) {
					Image existingImage = imageRepository.findById(imageDto.id())
							.orElseThrow(() -> new RessourceNotFoundException(
									"L'image avec l'id " + imageDto.id() + " n'a pas été trouvé"));
					validImages.add(existingImage);
				} else {
					Image newImageToSave = CreateImageDto.convertToEntity(imageDto);
					Image savedImage = imageRepository.save(newImageToSave);
					validImages.add(savedImage);
				}
			}
			article.setImages(validImages);

		Article savedArticle = articleRepository.save(article);

			for (AuthorContributionDto authoContribution : articleDto.authors()) {
				Author author = authorRepository.findById(authoContribution.authorId()).orElseThrow(() -> new RessourceNotFoundException(
						"L'autheur avec l'id " + authoContribution.authorId() + " n'a pas été trouvé"));
				ArticleAuthor articleAuthor = new ArticleAuthor();
				articleAuthor.setAuthor(author);
				articleAuthor.setArticle(savedArticle);
				articleAuthor.setContribution(authoContribution.contribution());
				articleAuthorRepository.save(articleAuthor);
			}

		return ArticleDto.convertToDTO(savedArticle);
	}

	public ArticleDto updateArticle(Long id, CreateArticleDto articleDetails) {
	
		Article article = articleRepository.findById(id)
				.orElseThrow(() -> new RessourceNotFoundException("L'article avec l'id " + id + " n'a pas été trouvé"));
	
		
		article.setTitle(articleDetails.title());
		article.setContent(articleDetails.content());
		article.setUpdatedAt(LocalDateTime.now());

			Category category = categoryRepository.findById(articleDetails.categoryId())
					.orElseThrow(() -> new RessourceNotFoundException(
							"La category avec l'id " + articleDetails.categoryId() + " n'a pas été trouvé"));
			article.setCategory(category);
		


			List<Image> validImages = new ArrayList<>();
			for (CreateImageDto image : articleDetails.images()) {
				if (image.id() != null) {
					Image existingImage = imageRepository.findById(image.id())
							.orElseThrow(() -> new RessourceNotFoundException(
									"L'image avec l'id " + image.id() + " n'a pas été trouvé"));
					validImages.add(existingImage);
				} else {
					Image savedImage = CreateImageDto.convertToEntity(image);
					validImages.add(savedImage);
				}
			}
			article.setImages(validImages);
		
			for (ArticleAuthor oldArticleAuthor : article.getArticleAuthors()) {
				articleAuthorRepository.delete(oldArticleAuthor);
			}

			List<ArticleAuthor> updatedArticleAuthors = new ArrayList<>();

			for (AuthorContributionDto articleAuthorDetails : articleDetails.authors()) {
				Author author = authorRepository.findById(articleAuthorDetails.authorId()).orElseThrow(() -> new RessourceNotFoundException(
						"L'autheur avec l'id " + articleAuthorDetails.authorId() + " n'a pas été trouvé"));

				ArticleAuthor newArticleAuthor = new ArticleAuthor();
				newArticleAuthor.setAuthor(author);
				newArticleAuthor.setArticle(article);
				newArticleAuthor.setContribution(articleAuthorDetails.contribution());

				updatedArticleAuthors.add(newArticleAuthor);
			}

			for (ArticleAuthor articleAuthor : updatedArticleAuthors) {
				articleAuthorRepository.save(articleAuthor);
			}

			article.setArticleAuthors(updatedArticleAuthors);
		

		Article updatedArticle = articleRepository.save(article);
		return ArticleDto.convertToDTO(updatedArticle);
	}

	public boolean deleteArticle(Long id) {
		Article article = articleRepository.findById(id)
				.orElseThrow(() -> new RessourceNotFoundException("L'autheur avec l'id " + id + " n'a pas été trouvé"));
		articleAuthorRepository.deleteAll(article.getArticleAuthors());
		articleRepository.delete(article);
		return true;
	}
}