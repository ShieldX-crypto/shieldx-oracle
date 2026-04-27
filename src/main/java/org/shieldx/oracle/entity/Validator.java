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
    private int maxDelegation;
    private int totalSlash;
    private int totalValidatorSuccess;
    private int totalValidatorFailure;
    private int totalLeaderSuccess;
    private int totalLeaderFailure;
    private int totalSignaturesIgnored;
    private int commission;
    private ValidatorListStatus status;
    private boolean jailed;
    private Instant updatedAt = Instant.now();
}
