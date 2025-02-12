package org.wildcodeschool.myblog.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.wildcodeschool.myblog.model.User;

public record UserRegistrationDto(
		
		
		@NotBlank(message = "le nom de compte ne doit pas étre vide")
		String username,
		
		@Email(message =" format d'email non valide")
		@NotBlank(message = "l'email ne doit pas étre vide")
		String email,
		
		@NotBlank(message = "mot de passe manquant")
		String password
		
		) {
		public static User convertToEntity(UserRegistrationDto dto) {
			return new User(
					dto.username(),
					dto.email(),
					dto.password()
					);
		}
}
