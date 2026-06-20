package com.accountplace.api.controller.auth;

import com.accountplace.api.dto.create.CreateUserDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Routes publiques — aucune authentification requise.
 * Préfixe : /api/auth
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    // TODO: injecter AuthService

    /**
     * POST /api/auth/register
     * Crée un compte utilisateur et retourne les tokens.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody CreateUserDTO dto) {
        // TODO: return authService.register(dto)
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * POST /api/auth/login
     * Authentifie un utilisateur (username ou email + password).
     * Body attendu : { "login": "...", "password": "..." }
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody /* LoginRequestDTO */ Object body) {
        // TODO: return authService.login(body)
        return ResponseEntity.ok().build();
    }

    /**
     * POST /api/auth/refresh
     * Échange un refresh token contre un nouvel access token.
     * Body attendu : { "refreshToken": "..." }
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody /* RefreshTokenDTO */ Object body) {
        // TODO: return authService.refresh(body)
        return ResponseEntity.ok().build();
    }

    /**
     * POST /api/auth/logout
     * Révoque le refresh token côté serveur.
     * Le client doit supprimer l'access token lui-même.
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody /* RefreshTokenDTO */ Object body) {
        // TODO: authService.logout(body)
        return ResponseEntity.noContent().build();
    }
}
