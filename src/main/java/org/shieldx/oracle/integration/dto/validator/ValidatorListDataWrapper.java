package org.shieldx.oracle.integration.dto.validator;

import lombok.Data;
import org.shieldx.oracle.integration.dto.ApiData;

import java.util.List;

@Data
public class ValidatorListDataWrapper implements ApiData {
    private List<ValidatorDto> validators;
}
