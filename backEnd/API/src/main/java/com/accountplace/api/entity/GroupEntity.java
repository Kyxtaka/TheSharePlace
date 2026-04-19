package com.accountplace.api.entity;


import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "GROUPS")
@Data
@NoArgsConstructor
public class GroupEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name  = "unique_id", nullable = false, unique = true)
    private Long UID;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "group_description")
    private String group_description;

    public GroupEntity(Long UID, String name, String password, String group_description) {
        this.UID = UID;
        this.name = name;
        this.password = password;
        this.group_description = group_description;
    }

}
