package com.accountplace.api.dto.create;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder
public record CreateGroupJoinRequestDTO(

        @NotNull(message = "Group UUID is required")
        UUID groupUuid,

        /**
         * Password required only when the group is password-protected.
         * Will be validated against the stored group vault key server-side.
         */
        String requestPassword
) {}
