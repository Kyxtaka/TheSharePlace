package com.accountplace.api.dto.register;

import lombok.Data;

/**
 * Data Transfer Object (DTO) for registering a new platform.
 * This class encapsulates the necessary information for creating a platform entry.
 */
@Data
public class RegisterPlatformDTO {

    /**
     * The name of the platform.
     */
    private String name;

    /**
     * The URL of the platform.
     */
    private String url;

    /**
     * The image URL or path associated with the platform.
     * Defaults to an empty string if not provided.
     */
    private String img = "";
}
