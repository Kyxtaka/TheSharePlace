package com.accountplace.api.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "platforms")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PlatformEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "platform_id")
    @EqualsAndHashCode.Include
    private Integer id;

    @Column(name = "uuid", nullable = false, unique = true, updatable = false)
    private UUID uuid =  UUID.randomUUID();

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "website_url")
    private String websiteUrl;

    @Column(name = "img_url")
    private String imgUrl;
}
