package com.accountplace.api.dto.update;

import jakarta.validation.constraints.Email;
import lombok.Builder;

import java.util.Optional;
import java.util.UUID;

@Builder
public record UpdateAccountDTO(

        Optional<String> username,

        @Email
        Optional<String> email,

        /**
         * New plain-text password — will be re-encrypted with the group vault key.
         */
        Optional<String> password,

        Optional<Boolean> a2fEnabled,

        /**
         * Moving the account to a different platform.
         */
        Optional<UUID> platformUuid
) {}
