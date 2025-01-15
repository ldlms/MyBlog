package org.wildcodeschool.myblog.dto;

import java.util.List;

import org.wildcodeschool.myblog.model.Author;

public record AuthorDto(

	Long id,
	String firstname,
	String lastname,
	List<String> articleTitle
) {
	public static AuthorDto convertToDTO(Author author) {
		return new AuthorDto(
				author.getId(),
				author.getFirstname(),
				author.getLastname(),
				author.getArticleAuthors() != null ?  author.getArticleAuthors().stream().map(article -> article.getArticle().getTitle()).toList():null
				);
				
	};
}
