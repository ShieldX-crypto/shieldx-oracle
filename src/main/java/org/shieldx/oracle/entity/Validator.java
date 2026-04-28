package org.shieldx.oracle.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Getter
@Setter
@Table(name = "validators")
@EqualsAndHashCode(exclude = {"id", "updatedAt"})
public class Validator {
    @Id
    private Long id;
    private String owner;
    private String name;
    private long totalStake;
    private long selfStake;
    private boolean canDelegate;
    private int numJailed;
    private long maxDelegation;
    private long totalSlash;
    private long totalValidatorSuccess;
    private long totalValidatorFailure;
    private long totalLeaderSuccess;
    private long totalLeaderFailure;
    private long totalSignaturesIgnored;
    private int commission;
    private ValidatorListStatus status;
    private boolean jailed;
    private Instant updatedAt = Instant.now();
}
