package com.accountplace.api.controller;


import com.accountplace.api.entity.Role;
import com.accountplace.api.entity.UserEntity;
import com.accountplace.api.dto.reponse.AuthResponseDto;
import com.accountplace.api.dto.auth.LoginDto;
import com.accountplace.api.dto.register.RegisterUserBodyDTO;
import com.accountplace.api.repositories.RoleRepository;
import com.accountplace.api.repositories.UserRepository;
import com.accountplace.api.security.JWTProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTProvider jwtProvider;

    @Autowired
    private AuthController(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            JWTProvider jwtProvider
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody RegisterUserBodyDTO registerUserBodyDTO) {
        if (userRepository.existsByEmail(registerUserBodyDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonMap("message", "Email already exists"));
        } else if (userRepository.existsByUsername(registerUserBodyDTO.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonMap("message", "Username already exists"));
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(registerUserBodyDTO.getEmail());
        userEntity.setUsername(registerUserBodyDTO.getUsername());
        userEntity.setFirstname(registerUserBodyDTO.getFirstname());
        userEntity.setLastname(registerUserBodyDTO.getLastname());
        userEntity.setPassword(passwordEncoder.encode(registerUserBodyDTO.getPassword()));
        Role roles = roleRepository.findByName("USER").get();
        userEntity.setRoles(Collections.singletonList(roles));
        userRepository.save(userEntity);
        return ResponseEntity.ok(Collections.singletonMap("message", "User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getIdentifier(),
                        loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);
        return new ResponseEntity<>(new AuthResponseDto(token), HttpStatus.OK);
    }


}
