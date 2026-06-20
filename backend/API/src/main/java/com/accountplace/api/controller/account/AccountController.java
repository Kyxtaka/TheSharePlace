package com.accountplace.api.controller.account;

import com.accountplace.api.dto.create.CreateAccountDTO;
import com.accountplace.api.dto.update.UpdateAccountDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Préfixe : /api/groups/{groupUuid}/accounts
 *
 * Les comptes sont toujours scopés à un groupe.
 * Le service vérifie que l'utilisateur est bien membre du groupe avant chaque opération.
 */
@RestController
@RequestMapping("/api/groups/{groupUuid}/accounts")
@RequiredArgsConstructor
public class AccountController {

    // TODO: injecter AccountService

    /**
     * GET /api/groups/{groupUuid}/accounts
     * Liste tous les comptes partagés d'un groupe.
     * Accessible aux membres du groupe.
     */
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> listByGroup(@PathVariable UUID groupUuid) {
        // TODO: vérifier membership dans le service
        // TODO: return accountService.listByGroup(groupUuid, principal)
        return ResponseEntity.ok().build();
    }

    /**
     * GET /api/groups/{groupUuid}/accounts/{uuid}
     * Récupère un compte partagé (avec le mot de passe déchiffré).
     * Accessible aux membres du groupe.
     */
    @GetMapping("/{uuid}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getByUuid(@PathVariable UUID groupUuid,
                                        @PathVariable UUID uuid) {
        // TODO: vérifier que le compte appartient au groupe
        // TODO: return accountService.getByUuid(groupUuid, uuid, principal)
        return ResponseEntity.ok().build();
    }

    /**
     * POST /api/groups/{groupUuid}/accounts
     * Crée un nouveau compte partagé dans le groupe.
     * Le mot de passe sera chiffré avec la vault key du groupe côté service.
     */
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> create(@PathVariable UUID groupUuid,
                                     @Valid @RequestBody CreateAccountDTO dto) {
        // TODO: vérifier membership + chiffrement vault key dans le service
        // TODO: return accountService.create(groupUuid, dto, principal)
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * PATCH /api/groups/{groupUuid}/accounts/{uuid}
     * Met à jour partiellement un compte partagé.
     * Si le mot de passe change, il sera re-chiffré avec la vault key.
     */
    @PatchMapping("/{uuid}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> update(@PathVariable UUID groupUuid,
                                     @PathVariable UUID uuid,
                                     @Valid @RequestBody UpdateAccountDTO dto) {
        // TODO: vérifier membership + ownership du compte dans le groupe
        // TODO: return accountService.update(groupUuid, uuid, dto, principal)
        return ResponseEntity.ok().build();
    }

    /**
     * DELETE /api/groups/{groupUuid}/accounts/{uuid}
     * Supprime (archive GDPR) un compte partagé.
     * Réservé aux admins du groupe.
     */
    @DeleteMapping("/{uuid}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> delete(@PathVariable UUID groupUuid,
                                        @PathVariable UUID uuid) {
        // TODO: vérifier que principal est admin du groupe
        // TODO: accountService.delete(groupUuid, uuid, principal)
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/groups/{groupUuid}/accounts/{uuid}/history
     * Retourne l'historique des modifications d'un compte.
     */
    @GetMapping("/{uuid}/history")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getHistory(@PathVariable UUID groupUuid,
                                         @PathVariable UUID uuid) {
        // TODO: vérifier membership
        // TODO: return accountService.getHistory(groupUuid, uuid, principal)
        return ResponseEntity.ok().build();
    }
}
