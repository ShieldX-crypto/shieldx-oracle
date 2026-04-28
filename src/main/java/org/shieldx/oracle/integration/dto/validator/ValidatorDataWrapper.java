package org.shieldx.oracle.integration.dto.validator;

import lombok.Data;
import org.shieldx.oracle.integration.dto.ApiData;

@Data
public class ValidatorDataWrapper implements ApiData {
    private ValidatorDto validator;
}
