package org.wildcodeschool.myblog.dto;

import java.util.List;

import org.wildcodeschool.myblog.model.Category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record CreateCategoryDto(
		
		@NotBlank(message = "Le nom de la categorie ne doit pas étre nul")
		String name,
		
		@NotEmpty(message = "la liste des articles ne doit pas étre vide")
		List<Long> articleId
		
		) {
		public static Category convertToEntity(CreateCategoryDto dto) {
			return new Category(dto.name());
		}
}
