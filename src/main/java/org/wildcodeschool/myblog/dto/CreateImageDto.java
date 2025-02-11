package org.wildcodeschool.myblog.dto;

import org.wildcodeschool.myblog.model.Image;

import jakarta.validation.constraints.NotBlank;

public record CreateImageDto (

	
	@NotBlank(message = "L'url est vide !")
	String url
) {
	public static Image convertToEntity(ImageDto imageDto) {
		return new Image(imageDto.url());
	}
}
