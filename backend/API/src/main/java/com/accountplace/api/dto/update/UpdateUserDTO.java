package com.accountplace.api.dto.update;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.Optional;

/**
 * Partial update DTO for a user.
 *
 * Each field is wrapped in {@link Optional} to distinguish three states:
 * <ul>
 *   <li>{@code null} (absent from JSON)   → field not provided, do NOT update</li>
 *   <li>{@code Optional.empty()}          → client explicitly sends {@code null}, clear the value</li>
 *   <li>{@code Optional.of(value)}        → update to this value</li>
 * </ul>
 *
 * Use with a custom Jackson deserializer or spring-boot-starter-validation
 * + {@code @JsonDeserialize} to honour absent vs null properly.
 */
@Builder
public record UpdateUserDTO(

        @Size(min = 3, max = 255)
        Optional<String> username,

        @Email
        Optional<String> email,

        /**
         * Raw new password — hash before persisting. {@code null} means no change.
         */
        @Size(min = 8)
        Optional<String> password,

        Optional<String> firstname,

        Optional<String> lastname
) {
    /** Convenience: true when the record carries at least one non-null Optional. */
    public boolean hasAnyField() {
        return username != null || email != null || password != null
                || firstname != null || lastname != null;
    }
}
