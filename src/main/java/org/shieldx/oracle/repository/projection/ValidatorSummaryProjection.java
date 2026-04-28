package org.shieldx.oracle.repository.projection;

public interface ValidatorSummaryProjection {
    String getOwner();
    String getName();
    String getStatus();
    boolean isJailed();
    int getCommission();
    long getTotalStake();
    long getMaxDelegation();
    Integer getRiskScore();
    String getRiskModelVersion();
}
