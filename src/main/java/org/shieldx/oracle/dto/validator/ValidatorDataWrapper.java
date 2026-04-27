package org.shieldx.oracle.dto.validator;

import lombok.Data;
import org.shieldx.oracle.dto.ApiData;

@Data
public class ValidatorDataWrapper implements ApiData {
    private ValidatorDto validator;
}
