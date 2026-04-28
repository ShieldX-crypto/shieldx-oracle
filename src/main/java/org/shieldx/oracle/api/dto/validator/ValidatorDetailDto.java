package org.shieldx.oracle.api.dto.validator;

import org.shieldx.oracle.entity.ValidatorListStatus;

import java.time.Instant;

public record ValidatorDetailDto(
        String owner,
        String name,
        ValidatorListStatus status,
        boolean jailed,
        int commission,
        Integer riskScore,
        long totalStake,
        long selfStake,
        long remainingCapacity,

        int numJailed,
        int totalSlash,
        int totalSignaturesIgnored,
        int totalValidatorSuccess,
        int totalValidatorFailure,
        int totalLeaderSuccess,
        int totalLeaderFailure,

        Instant updatedAt
) {}
