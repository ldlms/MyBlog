package org.wildcodeschool.myblog.dto;

import java.util.List;

import org.wildcodeschool.myblog.model.Article;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CreateArticleDto (

	@NotBlank(message = "Le titre ne doit pas être vide")
    @Size(min = 2, max = 50, message = "Le titre doit contenir entre 2 et 50 caractères")
    String title,

    @NotBlank(message = "Le contenu ne doit pas être vide")
    @Size(min = 10, message = "Le contenu doit contenir au moins 10 caractères")
    String content,

    @NotNull(message = "L'ID de la catégorie ne doit pas être nul")
    @Positive(message = "L'ID de la catégorie doit être un nombre positif")
    Long categoryId,

    @NotEmpty(message = "La liste des images ne doit pas être vide")
    List<@Valid ImageDto> images,

    @NotEmpty(message = "La liste des auteurs ne doit pas être vide")
    List<@Valid AuthorContributionDto> authors
) {
	public static Article convertToEntity(CreateArticleDto dto) {
		return new Article(
				dto.title(),
				dto.content()
				
				);
	};
}
	
	
	



