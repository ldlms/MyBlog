package org.wildcodeschool.myblog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ArticleContributionDto(
		
		@NotNull(message = "L'ID de l'article ne doit pas être nul")
	    @Positive(message = "L'ID de l'article doit être un nombre positif")
		Long articleId,
		
		@NotBlank(message = "La contribution de l'article ne doit pas être vide")
	    String contribution) {
	
}
