package com.accountplace.api.dto.update;

import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.Optional;

@Builder
public record UpdateGroupDTO(

        @Size(max = 255)
        Optional<String> name,

        @Size(max = 255)
        Optional<String> groupDescription,

        /**
         * New vault key (plain-text) — triggers key rotation server-side:
         * all account passwords in the group must be re-encrypted.
         * Only admin-level callers should be allowed to set this.
         */
        Optional<String> vaultKey
) {}
