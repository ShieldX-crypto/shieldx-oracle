package org.shieldx.oracle.dto.validator;

import lombok.Data;

@Data
public class SuccessRate {
    private long numSuccess;
    private long numFailure;
}
