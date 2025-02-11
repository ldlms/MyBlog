package org.wildcodeschool.myblog.dto;

import java.util.List;

import org.wildcodeschool.myblog.model.Image;

import jakarta.validation.constraints.NotBlank;

public record CreateImageDto (

	Long id,
	
	@NotBlank(message = "L'url est vide !")
	String url,
	
	List<CreateArticleDto> articles
) {
	public static Image convertToEntity(CreateImageDto imageDto) {
		return new Image(imageDto.url());
	}
}
