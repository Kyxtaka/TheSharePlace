package com.accountplace.api.dto.update;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.hibernate.validator.constraints.URL;

import java.util.Optional;

@Builder
public record UpdatePlatformDTO(

        @Size(max = 255)
        Optional<String> name,

        @URL
        Optional<String> websiteUrl,

        @URL
        Optional<String> imgUrl
) {}
