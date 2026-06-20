package com.accountplace.api.controller.user;

import com.accountplace.api.dto.update.UpdateUserDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Préfixe : /api/users
 *
 * Convention de rôles :
 *  - ROLE_ADMIN  → gestion globale de tous les utilisateurs
 *  - ROLE_USER   → accès à son propre profil uniquement
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    // TODO: injecter UserService

    // -------------------------------------------------------------------------
    // Profil personnel — ROLE_USER
    // -------------------------------------------------------------------------

    /**
     * GET /api/users/me
     * Retourne le profil de l'utilisateur connecté.
     */
    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getMe() {
        // TODO: return userService.getMe(principal)
        return ResponseEntity.ok().build();
    }

    /**
     * PATCH /api/users/me
     * Met à jour partiellement le profil de l'utilisateur connecté.
     */
    @PatchMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateMe(@Valid @RequestBody UpdateUserDTO dto) {
        // TODO: return userService.updateMe(principal, dto)
        return ResponseEntity.ok().build();
    }

    /**
     * DELETE /api/users/me
     * Supprime le compte de l'utilisateur connecté (self-delete).
     */
    @DeleteMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteMe() {
        // TODO: userService.deleteMe(principal)
        return ResponseEntity.noContent().build();
    }

    // -------------------------------------------------------------------------
    // Administration globale — ROLE_ADMIN
    // -------------------------------------------------------------------------

    /**
     * GET /api/users
     * Liste tous les utilisateurs (paginée).
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> listAll() {
        // TODO: return userService.listAll(pageable)
        return ResponseEntity.ok().build();
    }

    /**
     * GET /api/users/{uuid}
     * Récupère un utilisateur par son UUID.
     */
    @GetMapping("/{uuid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getByUuid(@PathVariable UUID uuid) {
        // TODO: return userService.getByUuid(uuid)
        return ResponseEntity.ok().build();
    }

    /**
     * PATCH /api/users/{uuid}
     * Met à jour partiellement un utilisateur (admin seulement).
     */
    @PatchMapping("/{uuid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@PathVariable UUID uuid,
                                    @Valid @RequestBody UpdateUserDTO dto) {
        // TODO: return userService.update(uuid, dto)
        return ResponseEntity.ok().build();
    }

    /**
     * DELETE /api/users/{uuid}
     * Supprime un utilisateur (admin seulement).
     */
    @DeleteMapping("/{uuid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID uuid) {
        // TODO: userService.delete(uuid)
        return ResponseEntity.noContent().build();
    }

    /**
     * POST /api/users/{uuid}/roles/{roleUuid}
     * Assigne un rôle global à un utilisateur.
     */
    @PostMapping("/{uuid}/roles/{roleUuid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> assignRole(@PathVariable UUID uuid,
                                           @PathVariable UUID roleUuid) {
        // TODO: userService.assignRole(uuid, roleUuid)
        return ResponseEntity.noContent().build();
    }

    /**
     * DELETE /api/users/{uuid}/roles/{roleUuid}
     * Retire un rôle global à un utilisateur.
     */
    @DeleteMapping("/{uuid}/roles/{roleUuid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> removeRole(@PathVariable UUID uuid,
                                           @PathVariable UUID roleUuid) {
        // TODO: userService.removeRole(uuid, roleUuid)
        return ResponseEntity.noContent().build();
    }
}
