package com.accountplace.api.dto.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CreateRoleDTO(
        @NotBlank(message = "Role name is required")
        @Size(max = 255, message = "Role name must not exceed 255 characters")
        String roleName
) {}
