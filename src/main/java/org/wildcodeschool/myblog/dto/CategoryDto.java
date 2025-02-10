package org.wildcodeschool.myblog.dto;

import java.util.List;

import org.wildcodeschool.myblog.model.Article;
import org.wildcodeschool.myblog.model.Category;

public record CategoryDto(Long id,String name,List<String> articles) {
	
	public static CategoryDto convertToDto(Category category) {
		return new CategoryDto(
				category.getId(),
				category.getName(),
				category.getArticles() != null ? category.getArticles().stream().map(Article::getTitle).toList() : null
				);
	}
}
