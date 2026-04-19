package com.accountplace.api.controller.open;


import com.accountplace.api.dto.crud.create.UserCreateDTO;
import com.accountplace.api.dto.response.auth.AuthResponseDTO;
import com.accountplace.api.dto.requestBody.auth.LoginDTO;
import com.accountplace.api.exceptions.auth.UserAlreadyExistException;
import com.accountplace.api.security.JWTProvider;
import com.accountplace.api.service.crud.UserCrudService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.accountplace.api.tools.NetworkToolsLib;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JWTProvider jwtProvider;
    private final UserCrudService userCrudService;

    @Autowired
    private AuthController(
            AuthenticationManager authenticationManager,
            JWTProvider jwtProvider,
            UserCrudService userCrudService
    ) {
        this.authenticationManager = authenticationManager;

        this.jwtProvider = jwtProvider;
        this.userCrudService = userCrudService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody UserCreateDTO userCreateDTO) {
        try {
            this.userCrudService.create(userCreateDTO);
        } catch (UserAlreadyExistException e) {
            if (e.getType().equals("EMAIL")) return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonMap("message", "Email already exists"));
            else if (e.getType().equals("USERNAME")) return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonMap("message", "Username already exists"));
        }
        return ResponseEntity.ok(Collections.singletonMap("message", "User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDTO loginDto, HttpServletRequest request){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getIdentifier(),
                        loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String rawIP = NetworkToolsLib.getClientIpAddress(request);
        String userAgent = NetworkToolsLib.getUserAgent(request);
        String token = jwtProvider.generateToken(authentication, rawIP, userAgent);
        System.out.println("got from Ip address: "+NetworkToolsLib.getClientIpAddress(request));
        //return new ResponseEntity<>(new AuthResponseDTO(token), HttpStatus.OK);
        return ResponseEntity.ok(new AuthResponseDTO(token));
    }
}
