package org.wildcodeschool.myblog.dto;

import org.wildcodeschool.myblog.model.ArticleAuthor;

public record ArticleAuthorDto (
	Long id,
	String articleTitle,
	String authorLastname,
	String contribution
) {
	public static ArticleAuthorDto convertToDTO(ArticleAuthor articleAuthor) {
		return new ArticleAuthorDto(
		articleAuthor.getId(), 
		articleAuthor.getArticle().getTitle(),
		articleAuthor.getAuthor().getLastname(),
		articleAuthor.getContribution()
		);
	}
}
