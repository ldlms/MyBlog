package org.wildcodeschool.myblog.controller;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wildcodeschool.myblog.dto.UserDto;
import org.wildcodeschool.myblog.dto.UserRegistrationDto;
import org.wildcodeschool.myblog.model.User;
import org.wildcodeschool.myblog.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody UserRegistrationDto userRegistrationDto) {
    	User registeredUser = userService.registerUser(userRegistrationDto);
    	
    	UserDto returnedUser = UserDto.convertToDto(registeredUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(returnedUser);
    }
}
