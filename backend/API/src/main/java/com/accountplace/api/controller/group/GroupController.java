package com.accountplace.api.controller.group;

import com.accountplace.api.dto.create.CreateGroupDTO;
import com.accountplace.api.dto.create.CreateGroupJoinRequestDTO;
import com.accountplace.api.dto.update.UpdateGroupDTO;
import com.accountplace.api.dto.update.UpdateGroupJoinRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Préfixe : /api/groups
 *
 * Convention de rôles :
 *  - ROLE_ADMIN          → gestion globale (plateforme)
 *  - GROUP_ADMIN:{uuid}  → admin d'un groupe spécifique (vérifié dans le service)
 *  - ROLE_USER           → membre ou candidat
 */
@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    // TODO: injecter GroupService, GroupJoinRequestService

    // -------------------------------------------------------------------------
    // Groupes — ROLE_USER
    // -------------------------------------------------------------------------

    /**
     * GET /api/groups
     * Retourne les groupes dont l'utilisateur connecté est membre.
     */
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getMyGroups() {
        // TODO: return groupService.getGroupsForCurrentUser(principal)
        return ResponseEntity.ok().build();
    }

    /**
     * POST /api/groups
     * Crée un nouveau groupe. Le créateur devient automatiquement admin du groupe.
     */
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> create(@Valid @RequestBody CreateGroupDTO dto) {
        // TODO: return groupService.create(dto, principal)
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * GET /api/groups/{uuid}
     * Récupère les détails d'un groupe.
     * Accessible uniquement aux membres du groupe.
     */
    @GetMapping("/{uuid}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getByUuid(@PathVariable UUID uuid) {
        // TODO: vérifier membership dans le service
        // TODO: return groupService.getByUuid(uuid, principal)
        return ResponseEntity.ok().build();
    }

    /**
     * PATCH /api/groups/{uuid}
     * Met à jour partiellement un groupe.
     * Réservé aux admins du groupe ou aux ROLE_ADMIN globaux.
     */
    @PatchMapping("/{uuid}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> update(@PathVariable UUID uuid,
                                    @Valid @RequestBody UpdateGroupDTO dto) {
        // TODO: vérifier que principal est admin du groupe dans le service
        // TODO: return groupService.update(uuid, dto, principal)
        return ResponseEntity.ok().build();
    }

    /**
     * DELETE /api/groups/{uuid}
     * Supprime un groupe et tous ses comptes (cascade).
     * Réservé aux ROLE_ADMIN globaux.
     */
    @DeleteMapping("/{uuid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID uuid) {
        // TODO: groupService.delete(uuid)
        return ResponseEntity.noContent().build();
    }

    // -------------------------------------------------------------------------
    // Membres — gestion
    // -------------------------------------------------------------------------

    /**
     * GET /api/groups/{uuid}/members
     * Liste les membres d'un groupe avec leurs rôles.
     */
    @GetMapping("/{uuid}/members")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getMembers(@PathVariable UUID uuid) {
        // TODO: vérifier membership
        // TODO: return groupService.getMembers(uuid, principal)
        return ResponseEntity.ok().build();
    }

    /**
     * PATCH /api/groups/{uuid}/members/{userUuid}/role/{roleUuid}
     * Change le rôle d'un membre dans le groupe.
     * Réservé aux admins du groupe.
     */
    @PatchMapping("/{uuid}/members/{userUuid}/role/{roleUuid}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> changeMemberRole(@PathVariable UUID uuid,
                                                  @PathVariable UUID userUuid,
                                                  @PathVariable UUID roleUuid) {
        // TODO: vérifier que principal est admin du groupe
        // TODO: groupService.changeMemberRole(uuid, userUuid, roleUuid, principal)
        return ResponseEntity.noContent().build();
    }

    /**
     * DELETE /api/groups/{uuid}/members/{userUuid}
     * Expulse un membre du groupe (kick).
     * Réservé aux admins du groupe.
     */
    @DeleteMapping("/{uuid}/members/{userUuid}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> kickMember(@PathVariable UUID uuid,
                                            @PathVariable UUID userUuid) {
        // TODO: vérifier que principal est admin du groupe
        // TODO: groupService.kickMember(uuid, userUuid, principal)
        return ResponseEntity.noContent().build();
    }

    /**
     * DELETE /api/groups/{uuid}/members/me
     * Quitter le groupe volontairement (leave).
     */
    @DeleteMapping("/{uuid}/members/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> leaveGroup(@PathVariable UUID uuid) {
        // TODO: groupService.leave(uuid, principal)
        return ResponseEntity.noContent().build();
    }

    // -------------------------------------------------------------------------
    // Demandes d'adhésion (join requests)
    // -------------------------------------------------------------------------

    /**
     * POST /api/groups/{uuid}/join-requests
     * Envoie une demande pour rejoindre un groupe.
     */
    @PostMapping("/{uuid}/join-requests")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> requestJoin(@PathVariable UUID uuid,
                                          @Valid @RequestBody CreateGroupJoinRequestDTO dto) {
        // TODO: return joinRequestService.create(uuid, dto, principal)
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * GET /api/groups/{uuid}/join-requests
     * Liste les demandes d'adhésion en attente.
     * Réservé aux admins du groupe.
     */
    @GetMapping("/{uuid}/join-requests")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> listJoinRequests(@PathVariable UUID uuid) {
        // TODO: vérifier que principal est admin du groupe
        // TODO: return joinRequestService.listPending(uuid, principal)
        return ResponseEntity.ok().build();
    }

    /**
     * PATCH /api/groups/{uuid}/join-requests/{requestUuid}
     * Approuve ou rejette une demande d'adhésion.
     * Réservé aux admins du groupe.
     */
    @PatchMapping("/{uuid}/join-requests/{requestUuid}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> handleJoinRequest(@PathVariable UUID uuid,
                                                @PathVariable UUID requestUuid,
                                                @Valid @RequestBody UpdateGroupJoinRequestDTO dto) {
        // TODO: vérifier que principal est admin du groupe
        // TODO: return joinRequestService.handle(uuid, requestUuid, dto, principal)
        return ResponseEntity.ok().build();
    }

    /**
     * DELETE /api/groups/{uuid}/join-requests/{requestUuid}
     * Annule sa propre demande d'adhésion.
     */
    @DeleteMapping("/{uuid}/join-requests/{requestUuid}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> cancelJoinRequest(@PathVariable UUID uuid,
                                                   @PathVariable UUID requestUuid) {
        // TODO: vérifier que la demande appartient au principal
        // TODO: joinRequestService.cancel(requestUuid, principal)
        return ResponseEntity.noContent().build();
    }
}
