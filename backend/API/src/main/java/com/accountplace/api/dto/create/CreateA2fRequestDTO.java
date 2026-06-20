package com.accountplace.api.dto.create;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder
public record CreateA2fRequestDTO(
        @NotNull(message = "Account UUID is required")
        UUID accountUuid
) {}
