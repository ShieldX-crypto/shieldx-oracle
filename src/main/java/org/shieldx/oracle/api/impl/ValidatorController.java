package org.shieldx.oracle.api.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shieldx.oracle.api.ValidatorOperation;
import org.shieldx.oracle.api.dto.PageResponse;
import org.shieldx.oracle.api.dto.validator.ValidatorFilter;
import org.shieldx.oracle.api.dto.validator.ValidatorSummaryDto;
import org.shieldx.oracle.service.ValidatorService;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ValidatorController implements ValidatorOperation {
    private final ValidatorService validatorService;

    @Override
    public Mono<PageResponse<ValidatorSummaryDto>> getValidators(ValidatorFilter filter) {
        return validatorService.findAll(filter);
    }
}
