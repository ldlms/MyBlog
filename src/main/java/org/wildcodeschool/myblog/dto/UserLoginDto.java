package org.wildcodeschool.myblog.dto;

import jakarta.validation.constraints.NotBlank;
import org.wildcodeschool.myblog.model.User;

public record UserLoginDto(

        @NotBlank(message = "le nom de compte ne doit pas étre vide")
        String username,

        @NotBlank(message = "mot de passe manquant")
        String password
){}
