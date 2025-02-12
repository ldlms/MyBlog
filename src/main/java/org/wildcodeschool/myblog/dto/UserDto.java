package org.wildcodeschool.myblog.dto;

import org.wildcodeschool.myblog.model.User;

public record UserDto(
		String email,
		
		String username
		) {
	public static UserDto convertToDto(User user) {
		return new UserDto(
				user.getEmail(),
				user.getUsername()
				);
	}
}
