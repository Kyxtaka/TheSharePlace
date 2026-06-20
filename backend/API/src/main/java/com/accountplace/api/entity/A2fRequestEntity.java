package com.accountplace.api.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "a2f_requests")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class A2fRequestEntity {

    public enum RequestStatus { pending, approved, rejected }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "a2f_request_id")
    @EqualsAndHashCode.Include
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    @ToString.Exclude
    private AccountEntity account;

    @Column(name = "request_uuid", nullable = false, unique = true, updatable = false)
    private UUID requestUuid = UUID.randomUUID();

    @Enumerated(EnumType.STRING)
    @Column(name = "request_status", nullable = false)
    @Builder.Default
    private RequestStatus requestStatus = RequestStatus.pending;

    /**
     * Magic-link token — store hashed, validate via hash comparison.
     */
    @Column(name = "request_magic_token")
    private String requestMagicToken;

    /**
     * PIN / OTP token — store hashed.
     */
    @Column(name = "request_pin_token")
    private String requestPinToken;

    @CreationTimestamp
    @Column(name = "requested_at", nullable = false, updatable = false)
    private LocalDateTime requestedAt;

    /**
     * Auto-set to requestedAt + 10 minutes by the DB default.
     */
    @Column(name = "expire_at")
    private LocalDateTime expireAt;

    @Column(name = "revoked", nullable = false)
    @Builder.Default
    private Boolean revoked = false;
}
