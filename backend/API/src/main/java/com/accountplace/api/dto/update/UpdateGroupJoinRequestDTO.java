package com.accountplace.api.dto.update;

import com.accountplace.api.entity.GroupJoinRequestEntity.RequestStatus;
import lombok.Builder;

import java.util.Optional;

/**
 * Allows an admin to approve/reject a join request, or revoke it manually.
 */
@Builder
public record UpdateGroupJoinRequestDTO(

        Optional<RequestStatus> requestStatus,

        Optional<Boolean> revoked
) {}
