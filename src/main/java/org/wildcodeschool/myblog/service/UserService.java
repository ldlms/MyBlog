package org.wildcodeschool.myblog.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.wildcodeschool.myblog.dto.UserDto;
import org.wildcodeschool.myblog.dto.UserRegistrationDto;
import org.wildcodeschool.myblog.model.User;
import org.wildcodeschool.myblog.repository.UserRepository;

@Service
public class UserService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	
	public User registerUser(UserRegistrationDto userRegistration) {
        if(userRepository.existsByEmail(userRegistration.email())) {
            throw new RuntimeException("Cet email est déjà utilisé");
        }

        User user = UserRegistrationDto.convertToEntity(userRegistration);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        Set<String> roles = new HashSet<>();
        roles.add("ROLE_USER"); 
        user.setRoles(roles);
        

        return userRepository.save(user);
    }

}
