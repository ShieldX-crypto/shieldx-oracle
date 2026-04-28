package org.shieldx.oracle.repository.projection;

import java.time.Instant;

public interface ValidatorDetailProjection {
    String getOwner();
    String getName();
    String getStatus();
    boolean isJailed();
    int getCommission();
    long getTotalStake();
    long getSelfStake();
    long getMaxDelegation();
    int getNumJailed();
    int getTotalSlash();
    int getTotalSignaturesIgnored();
    int getTotalValidatorSuccess();
    int getTotalValidatorFailure();
    int getTotalLeaderSuccess();
    int getTotalLeaderFailure();
    Double getJailScore();
    Double getUptimeScore();
    Double getSkinScore();
    Integer getRiskScore();
    String getRiskTier();
    String getRiskModelVersion();
    Instant getRiskCalculatedAt();
    Instant getUpdatedAt();
}