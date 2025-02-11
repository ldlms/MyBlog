package org.wildcodeschool.myblog.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.wildcodeschool.myblog.model.Article;
import org.wildcodeschool.myblog.model.ArticleAuthor;
import org.wildcodeschool.myblog.model.Author;
import org.wildcodeschool.myblog.model.Image;

import jakarta.validation.constraints.NotBlank;

public record ArticleDto(Long id, 
		String title, 
		
		@NotBlank()
		String content, 
		String categoryName, 
		List<String> imagesUrl,
		List<String> authorLastname) 
{
	public static ArticleDto convertToDTO(Article article) {
		return new ArticleDto(
				article.getId(), 
				article.getTitle(), 
				article.getContent(),
				article.getCategory() != null ? article.getCategory().getName() : null,
				article.getImages() != null ? article.getImages().stream().map(Image::getUrl).toList() : null,
				article.getArticleAuthors() != null
						? article.getArticleAuthors().stream().map(author -> author.getAuthor().getLastname()).toList()
						: null);
	};
}