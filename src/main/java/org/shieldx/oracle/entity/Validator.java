package org.shieldx.oracle.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "validators")
public class Validator {
    @Id
    private String owner;
    private String name;
    private long totalStake;
    private long selfStake;
    private int numJailed;
    private int totalSlash;
    private int totalValidatorSuccess;
    private int totalValidatorFailure;
    private int totalLeaderSuccess;
    private int totalLeaderFailure;
    private int totalSignaturesIgnored;
    private int commission;
    @Enumerated(EnumType.STRING)
    private ValidatorListStatus status;
    private boolean jailed;
    private Instant updatedAt;
}
