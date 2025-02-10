package org.wildcodeschool.myblog.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.wildcodeschool.myblog.dto.ArticleDto;
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

	public ArticleDto createArticle(Article article) {
		article.setCreatedAt(LocalDateTime.now());
		article.setUpdatedAt(LocalDateTime.now());
		if (article.getCategory() != null) {
			Category category = categoryRepository.findById(article.getCategory().getId())
					.orElseThrow(() -> new RessourceNotFoundException(
							"La category avec l'id " + article.getCategory().getId() + " n'a pas été trouvé"));
			article.setCategory(category);
		}
		if (article.getImages() != null && !article.getImages().isEmpty()) {
			List<Image> validImages = new ArrayList<>();
			for (Image image : article.getImages()) {
				if (image.getId() != null) {
					Image existingImage = imageRepository.findById(image.getId())
							.orElseThrow(() -> new RessourceNotFoundException(
									"L'image avec l'id " + article.getCategory().getId() + " n'a pas été trouvé"));
					validImages.add(existingImage);
				} else {
					Image savedImage = imageRepository.save(image);
					validImages.add(savedImage);
				}
			}
			article.setImages(validImages);
		}
		Article savedArticle = articleRepository.save(article);
		if (article.getArticleAuthors() != null) {
			for (ArticleAuthor articleAuthor : article.getArticleAuthors()) {
				Author author = articleAuthor.getAuthor();
				author = authorRepository.findById(author.getId()).orElseThrow(() -> new RessourceNotFoundException(
						"L'author avec l'id " + articleAuthor.getAuthor().getId() + " n'a pas été trouvé"));
				articleAuthor.setAuthor(author);
				articleAuthor.setArticle(savedArticle);
				articleAuthor.setContribution(articleAuthor.getContribution());
				articleAuthorRepository.save(articleAuthor);
			}
		}
		return ArticleDto.convertToDTO(savedArticle);
	}

	public ArticleDto updateArticle(Long id, Article articleDetails) {
		Article article = articleRepository.findById(id)
				.orElseThrow(() -> new RessourceNotFoundException("L'article avec l'id " + id + " n'a pas été trouvé"));
		article.setTitle(articleDetails.getTitle());
		article.setContent(articleDetails.getContent());
		article.setUpdatedAt(LocalDateTime.now());
		if (articleDetails.getCategory() != null) {
			Category category = categoryRepository.findById(articleDetails.getCategory().getId())
					.orElseThrow(() -> new RessourceNotFoundException(
							"La category avec l'id " + articleDetails.getCategory().getId() + " n'a pas été trouvé"));
			article.setCategory(category);
		}

		if (articleDetails.getImages() != null) {
			List<Image> validImages = new ArrayList<>();
			for (Image image : articleDetails.getImages()) {
				if (image.getId() != null) {
					Image existingImage = imageRepository.findById(image.getId())
							.orElseThrow(() -> new RessourceNotFoundException(
									"L'image avec l'id " + image.getId() + " n'a pas été trouvé"));
					validImages.add(existingImage);
				} else {
					Image savedImage = imageRepository.save(image);
					validImages.add(savedImage);
				}
			}
			article.setImages(validImages);
		} else {
			article.getImages().clear();
		}
		if (articleDetails.getArticleAuthors() != null) {
			for (ArticleAuthor oldArticleAuthor : article.getArticleAuthors()) {
				articleAuthorRepository.delete(oldArticleAuthor);
			}

			List<ArticleAuthor> updatedArticleAuthors = new ArrayList<>();

			for (ArticleAuthor articleAuthorDetails : articleDetails.getArticleAuthors()) {
				Author author = articleAuthorDetails.getAuthor();
				author = authorRepository.findById(author.getId()).orElseThrow(() -> new RessourceNotFoundException(
						"L'autheur avec l'id " + articleAuthorDetails.getAuthor().getId() + " n'a pas été trouvé"));

				ArticleAuthor newArticleAuthor = new ArticleAuthor();
				newArticleAuthor.setAuthor(author);
				newArticleAuthor.setArticle(article);
				newArticleAuthor.setContribution(articleAuthorDetails.getContribution());

				updatedArticleAuthors.add(newArticleAuthor);
			}

			for (ArticleAuthor articleAuthor : updatedArticleAuthors) {
				articleAuthorRepository.save(articleAuthor);
			}

			article.setArticleAuthors(updatedArticleAuthors);
		}

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