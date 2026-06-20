package com.accountplace.api.dto.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.hibernate.validator.constraints.URL;

@Builder
public record CreatePlatformDTO(

        @NotBlank(message = "Platform name is required")
        @Size(max = 255, message = "Platform name must not exceed 255 characters")
        String name,

        @URL(message = "Website URL must be a valid URL")
        String websiteUrl,

        @URL(message = "Image URL must be a valid URL")
        String imgUrl
) {}
