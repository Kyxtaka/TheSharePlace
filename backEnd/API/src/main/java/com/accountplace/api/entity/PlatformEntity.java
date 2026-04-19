package com.accountplace.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "PLATFORMS")
@Data
@NoArgsConstructor
public class PlatformEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "imgRef", nullable = true)
    private String imgRef;

    public PlatformEntity(String name, String url, String imgRef) {
        this.name = name;
        this.url = url;
        this.imgRef = imgRef;
    }
}
