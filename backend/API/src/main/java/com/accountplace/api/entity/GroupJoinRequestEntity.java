package com.accountplace.api.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "group_join_requests")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class GroupJoinRequestEntity {

    public enum RequestStatus { pending, approved, rejected }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_join_request_id")
    @EqualsAndHashCode.Include
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    @ToString.Exclude
    private GroupEntity group;

    @Enumerated(EnumType.STRING)
    @Column(name = "request_status", nullable = false)
    @Builder.Default
    private RequestStatus requestStatus = RequestStatus.pending;


    @Column(name = "request_uuid", nullable = false, unique = true, updatable = false)
    private UUID requestUuid =  UUID.randomUUID();

    /**
     * Optional password provided by the user to match the group's entry password.
     * Store hashed, never plain-text.
     */
    @Column(name = "request_password")
    private String requestPassword;

    @CreationTimestamp
    @Column(name = "requested_at", nullable = false, updatable = false)
    private LocalDateTime requestedAt;

    /**
     * Auto-set to requestedAt + 7 days by the DB default.
     * Can be overridden server-side if needed.
     */
    @Column(name = "expire_at")
    private LocalDateTime expireAt;

    @Column(name = "revoked", nullable = false)
    @Builder.Default
    private Boolean revoked = false;
}
