package org.shieldx.oracle.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Getter
@Setter
@ToString
@Table(name = "risk_scores")
public class RiskScore {
    @Id
    private Long id;
    private String validatorOwner;

    private double jailScore;
    private double uptimeScore;
    private double skinScore;
    private double totalScore;

    private RiskTier tier;
    private String modelVersion;
    private Instant calculatedAt;
}
