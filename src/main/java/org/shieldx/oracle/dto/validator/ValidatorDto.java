package org.shieldx.oracle.dto.validator;

import lombok.Data;

@Data
public class ValidatorDto {
    private String ownerAddress;
    private String blsPublicKey;
    private String rewardsAddress;
    private long registerNonce;
    private boolean selfStaked;
    private long selfStake;
    private long totalStake;
    private long jailedEpoch;
    private boolean jailed;
    private boolean waiting;
    private int numJailed;
    private long totalSlash;
    private boolean canDelegate;
    private long maxDelegation;
    private int commission;
    private long totalRewards;
    private String name;
    private String logo;
    private String list;
    private int index;
    private long accumulatedFees;
    private SuccessRate validatorSuccessRate;
    private SuccessRate leaderSuccessRate;
    private int validatorIgnoredSignaturesRate;
    private long rating;
    private long tempRating;
    private int numSelectedInSuccessBlocks;
    private int consecutiveProposerMisses;
    private SuccessRate totalValidatorSuccessRate;
    private SuccessRate totalLeaderSuccessRate;
    private long totalValidatorIgnoredSignaturesRate;
}
