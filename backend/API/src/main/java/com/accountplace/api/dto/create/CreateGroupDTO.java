package com.accountplace.api.dto.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CreateGroupDTO(

        @NotBlank(message = "Group name is required")
        @Size(max = 255, message = "Group name must not exceed 255 characters")
        String name,

        @Size(max = 255, message = "Description must not exceed 255 characters")
        String groupDescription,

        /**
         * Plain vault key provided at creation — will be hashed/encrypted server-side.
         * Optional: groups may have no vault key (open groups).
         */
        String vaultKey
) {}
