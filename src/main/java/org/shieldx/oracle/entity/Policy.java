package org.shieldx.oracle.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Policy {
    private String subjectKey;
    private CoveredEvent coveredEvent;
    private int durationEpochs;
    private long payout;
    private long premium;
    private long expiry;
    private long nonce;
    private String riskModelVersion;
}
