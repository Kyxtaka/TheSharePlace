package com.accountplace.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "ACCOUNTS")
@Data
@NoArgsConstructor
public class AccountEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "mail")
    private String mail;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "a2f")
    private Integer a2f;

    @Column(name = "platform_id", nullable = false)
    private Integer platform_id;

    @Column(name = "group_id", nullable = false)
    private Integer group_id;
}
