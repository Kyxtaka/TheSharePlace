package com.accountplace.api.dto.update;

import com.accountplace.api.entity.A2fRequestEntity.RequestStatus;
import lombok.Builder;

import java.util.Optional;

/**
 * Allows updating a 2FA request's status or revoking it.
 * Tokens (magic / pin) are never updated after creation — issue a new request instead.
 */
@Builder
public record UpdateA2fRequestDTO(

        Optional<RequestStatus> requestStatus,

        Optional<Boolean> revoked
) {}
