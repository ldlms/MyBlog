package org.wildcodeschool.myblog.dto;

import java.util.List;

import org.wildcodeschool.myblog.model.Author;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record CreateAuthorDto(
		@NotBlank() @Size(min = 2, max = 12, message = "Le firstname doit contenir entre 2 et 50 caractères") String firstname,

		@NotBlank() @Size(min = 2, max = 12, message = "Le lastname doit contenir entre 2 et 50 caractères") String lastname,

		@NotEmpty(message = "La liste des articles ne doit pas étre vide") List<@Valid ArticleContributionDto> article) {
	public static Author convertToEntity(CreateAuthorDto dto) {
		return new Author(dto.firstname(), dto.lastname());
	}

}
