package com.accountplace.api.dto.create;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder
public record CreateAccountDTO(

        String username,

        @Email(message = "Email must be valid")
        String email,

        /**
         * Plain-text password to be encrypted with the group vault key server-side.
         * Never log or persist this raw value.
         */
        @NotBlank(message = "Password is required")
        String password,

        boolean a2fEnabled,

        @NotNull(message = "Platform UUID is required")
        UUID platformUuid,

        @NotNull(message = "Group UUID is required")
        UUID groupUuid
) {}
