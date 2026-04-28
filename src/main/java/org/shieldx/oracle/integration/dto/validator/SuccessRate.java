package org.shieldx.oracle.integration.dto.validator;

import lombok.Data;

@Data
public class SuccessRate {
    private long numSuccess;
    private long numFailure;
}
