package com.accountplace.api.dto.review;

import lombok.*;

/**
 * Data Transfer Object (DTO) representing a platform.
 * This class encapsulates the details of a platform, including its ID, name, URL, and image reference.
 */
@Data
@AllArgsConstructor
public class PlatformDto {

    /**
     * The unique identifier for the platform.
     */
    private Integer plateformId;

    /**
     * The name of the platform.
     */
    private String plateformName;

    /**
     * The URL associated with the platform.
     */
    private String url;

    /**
     * A reference to the image associated with the platform.
     */
    private String imgRef;
}
