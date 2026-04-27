package org.shieldx.oracle.entity;

public enum ValidatorListStatus {
    ELIGIBLE,
    WAITING,
    JAILED,
    INACTIVE,
    ELECTED;

    public static ValidatorListStatus from(String value) {
        return value == null ? null : ValidatorListStatus.valueOf(value.toUpperCase());
    }
}
