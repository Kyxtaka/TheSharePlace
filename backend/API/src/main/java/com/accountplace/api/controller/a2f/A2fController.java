package com.accountplace.api.controller.a2f;

import com.accountplace.api.dto.create.CreateA2fRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Préfixe : /api/groups/{groupUuid}/accounts/{accountUuid}/a2f
 *
 * Flow 2FA pour les comptes partagés :
 *  1. Un membre demande un challenge 2FA  → POST /request
 *  2. Il valide via magic-link OU pin     → POST /verify/magic | /verify/pin
 *  3. En cas de problème, l'admin révoque → DELETE /{requestUuid}
 */
@RestController
@RequestMapping("/api/groups/{groupUuid}/accounts/{accountUuid}/a2f")
@RequiredArgsConstructor
public class A2fController {

    // TODO: injecter A2fService

    /**
     * POST /api/groups/{groupUuid}/accounts/{accountUuid}/a2f/request
     * Initie un challenge 2FA pour le compte partagé.
     * Génère un magic token ET un pin token, les envoie par le canal configuré.
     */
    @PostMapping("/request")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createRequest(@PathVariable UUID groupUuid,
                                            @PathVariable UUID accountUuid) {
        // TODO: vérifier membership du groupe dans le service
        // TODO: return a2fService.createRequest(groupUuid, accountUuid, principal)
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * POST /api/groups/{groupUuid}/accounts/{accountUuid}/a2f/verify/magic
     * Valide le challenge via le magic-link token.
     * Body attendu : { "token": "..." }
     */
    @PostMapping("/verify/magic")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> verifyMagicToken(@PathVariable UUID groupUuid,
                                               @PathVariable UUID accountUuid,
                                               @RequestBody /* TokenVerifyDTO */ Object body) {
        // TODO: return a2fService.verifyMagicToken(groupUuid, accountUuid, body)
        return ResponseEntity.ok().build();
    }

    /**
     * POST /api/groups/{groupUuid}/accounts/{accountUuid}/a2f/verify/pin
     * Valide le challenge via le code PIN/OTP.
     * Body attendu : { "pin": "..." }
     */
    @PostMapping("/verify/pin")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> verifyPinToken(@PathVariable UUID groupUuid,
                                             @PathVariable UUID accountUuid,
                                             @RequestBody /* TokenVerifyDTO */ Object body) {
        // TODO: return a2fService.verifyPinToken(groupUuid, accountUuid, body)
        return ResponseEntity.ok().build();
    }

    /**
     * GET /api/groups/{groupUuid}/accounts/{accountUuid}/a2f/requests
     * Liste les demandes 2FA d'un compte (pour les admins du groupe).
     */
    @GetMapping("/requests")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> listRequests(@PathVariable UUID groupUuid,
                                           @PathVariable UUID accountUuid) {
        // TODO: vérifier que principal est admin du groupe
        // TODO: return a2fService.listRequests(groupUuid, accountUuid, principal)
        return ResponseEntity.ok().build();
    }

    /**
     * DELETE /api/groups/{groupUuid}/accounts/{accountUuid}/a2f/{requestUuid}
     * Révoque manuellement une demande 2FA (admin du groupe).
     */
    @DeleteMapping("/{requestUuid}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> revokeRequest(@PathVariable UUID groupUuid,
                                               @PathVariable UUID accountUuid,
                                               @PathVariable UUID requestUuid) {
        // TODO: vérifier que principal est admin du groupe
        // TODO: a2fService.revoke(groupUuid, accountUuid, requestUuid, principal)
        return ResponseEntity.noContent().build();
    }
}
