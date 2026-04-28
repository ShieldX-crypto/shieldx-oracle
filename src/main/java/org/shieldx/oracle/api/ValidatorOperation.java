package org.shieldx.oracle.api;

import org.shieldx.oracle.api.dto.PageResponse;
import org.shieldx.oracle.api.dto.validator.ValidatorFilter;
import org.shieldx.oracle.api.dto.validator.ValidatorSummaryDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

import static org.shieldx.oracle.api.Constants.API_V1;
import static org.shieldx.oracle.api.Constants.VALIDATORS;

@RequestMapping(API_V1 + VALIDATORS)
public interface ValidatorOperation {

    @GetMapping
    Mono<PageResponse<ValidatorSummaryDto>> getValidators(ValidatorFilter filter);
}
