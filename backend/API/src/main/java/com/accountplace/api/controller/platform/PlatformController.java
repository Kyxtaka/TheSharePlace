package com.accountplace.api.controller.platform;

import com.accountplace.api.dto.create.CreatePlatformDTO;
import com.accountplace.api.dto.update.UpdatePlatformDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Préfixe : /api/platforms
 *
 * Convention de rôles :
 *  - GET  → accessible à tous les utilisateurs connectés (référentiel commun)
 *  - POST / PATCH / DELETE → réservé aux ROLE_ADMIN (gestion du catalogue)
 */
@RestController
@RequestMapping("/api/platforms")
@RequiredArgsConstructor
public class PlatformController {

    // TODO: injecter PlatformService

    /**
     * GET /api/platforms
     * Retourne toutes les plateformes disponibles.
     * Utilisé pour alimenter les selects lors de la création d'un compte partagé.
     */
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> listAll() {
        // TODO: return platformService.listAll()
        return ResponseEntity.ok().build();
    }

    /**
     * GET /api/platforms/{uuid}
     * Récupère une plateforme par son UUID.
     */
    @GetMapping("/{uuid}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getByUuid(@PathVariable UUID uuid) {
        // TODO: return platformService.getByUuid(uuid)
        return ResponseEntity.ok().build();
    }

    /**
     * POST /api/platforms
     * Crée une nouvelle plateforme dans le catalogue.
     * Réservé aux ROLE_ADMIN.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(@Valid @RequestBody CreatePlatformDTO dto) {
        // TODO: return platformService.create(dto)
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * PATCH /api/platforms/{uuid}
     * Met à jour partiellement une plateforme.
     * Réservé aux ROLE_ADMIN.
     */
    @PatchMapping("/{uuid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@PathVariable UUID uuid,
                                     @Valid @RequestBody UpdatePlatformDTO dto) {
        // TODO: return platformService.update(uuid, dto)
        return ResponseEntity.ok().build();
    }

    /**
     * DELETE /api/platforms/{uuid}
     * Supprime une plateforme si aucun compte actif ne la référence (RESTRICT).
     * Réservé aux ROLE_ADMIN.
     */
    @DeleteMapping("/{uuid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID uuid) {
        // TODO: platformService.delete(uuid)
        return ResponseEntity.noContent().build();
    }
}
