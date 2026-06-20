package com.accountplace.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "group_users", indexes = {
        @Index(name = "idx_group_users_user_id", columnList = "user_id")
}, uniqueConstraints = {
        @UniqueConstraint(columnNames = {"group_id", "user_id", "role_id"})
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class GroupUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_join_id")
    @EqualsAndHashCode.Include
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    @ToString.Exclude
    private GroupEntity group;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private UserEntity user;

    /** Role scopé au groupe (ex: admin, member, viewer…) */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    @ToString.Exclude
    private RoleEntity role;
}
