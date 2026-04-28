package org.shieldx.oracle.api.dto.validator;

import org.shieldx.oracle.entity.ValidatorListStatus;

public record ValidatorSummaryDto(
        String owner,
        String name,
        ValidatorListStatus status,
        boolean jailed,
        int commission,
        Integer riskScore,
        String riskModelVersion,
        long totalStake,
        long remainingCapacity
) {}