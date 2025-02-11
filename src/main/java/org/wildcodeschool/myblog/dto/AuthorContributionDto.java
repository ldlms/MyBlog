package org.wildcodeschool.myblog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AuthorContributionDto (

	@NotNull(message = "L'ID de l'auteur ne doit pas être nul")
    @Positive(message = "L'ID de l'auteur doit être un nombre positif")
    Long authorId,

    @NotBlank(message = "La contribution de l'auteur ne doit pas être vide")
    String contribution
) {
	
}
