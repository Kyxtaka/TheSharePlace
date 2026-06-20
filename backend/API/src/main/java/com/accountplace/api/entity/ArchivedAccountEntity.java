package com.accountplace.api.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * GDPR archive — accounts moved here before hard deletion.
 * FK to platform and group are nullable (ON DELETE SET NULL).
 */
@Entity
@Table(name = "archived_accounts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ArchivedAccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "archived_account_id")
    @EqualsAndHashCode.Include
    private Integer id;


    @Column(name = "uuid", nullable = false, unique = true, updatable = false)
    private UUID uuid = UUID.randomUUID();

    @Column(name = "username")
    private String username;

    @Column(name = "encrypted_password", columnDefinition = "TEXT")
    private String encryptedPassword;

    @Column(name = "email")
    private String email;

    @Column(name = "a2f_enabled", nullable = false)
    @Builder.Default
    private Boolean a2fEnabled = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "platform_id")   // ON DELETE SET NULL → nullable
    @ToString.Exclude
    private PlatformEntity platform;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")      // ON DELETE SET NULL → nullable
    @ToString.Exclude
    private GroupEntity group;

    @CreationTimestamp
    @Column(name = "archived_at", nullable = false, updatable = false)
    private LocalDateTime archivedAt;
}
