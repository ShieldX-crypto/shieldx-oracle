package org.shieldx.oracle.dto.validator;

import lombok.Data;
import org.shieldx.oracle.dto.ApiData;

import java.util.List;

@Data
public class ValidatorListDataWrapper implements ApiData {
    private List<ValidatorDto> validators;
}
