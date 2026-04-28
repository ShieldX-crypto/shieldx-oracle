package org.shieldx.oracle.api.dto.validator;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.shieldx.oracle.entity.ValidatorListStatus;

@Getter
@Setter
public class ValidatorFilter {
    @Min(0)
    private int page = 0;

    @Min(1)
    @Max(100)
    private int size = 20;

    private SortField sortBy = SortField.TOTAL_STAKE;
    private SortDirection direction = SortDirection.DESC;

    private Boolean jailed;
    private Boolean canDelegate;
    private ValidatorListStatus status;
    private Integer maxCommission;
    private Integer minRiskScore;
    private Integer maxRiskScore;

    public enum SortField {
        TOTAL_STAKE, SELF_STAKE, COMMISSION, RISK_SCORE,
        NUM_JAILED, TOTAL_VALIDATOR_SUCCESS
    }

    public enum SortDirection {
        ASC, DESC
    }
}
