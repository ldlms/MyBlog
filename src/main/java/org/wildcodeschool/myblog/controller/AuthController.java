package org.wildcodeschool.myblog.controller;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wildcodeschool.myblog.dto.UserDto;
import org.wildcodeschool.myblog.dto.UserLoginDto;
import org.wildcodeschool.myblog.dto.UserRegistrationDto;
import org.wildcodeschool.myblog.model.User;
import org.wildcodeschool.myblog.service.AuthenticationService;
import org.wildcodeschool.myblog.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    public AuthController(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody UserRegistrationDto userRegistrationDto) {
    	User registeredUser = userService.registerUser(userRegistrationDto);
    	
    	UserDto returnedUser = UserDto.convertToDto(registeredUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(returnedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticate(@RequestBody UserLoginDto userLoginDTO) {
        String token = authenticationService.authenticate(
                userLoginDTO.username(),
                userLoginDTO.password()
        );
        return ResponseEntity.ok(token);
    }
}
