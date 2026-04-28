package org.shieldx.oracle.service;

import org.shieldx.oracle.api.dto.PageResponse;
import org.shieldx.oracle.api.dto.validator.ValidatorFilter;
import org.shieldx.oracle.api.dto.validator.ValidatorSummaryDto;
import org.shieldx.oracle.entity.Validator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ValidatorService {
    Mono<Void> saveBatch(List<Validator> validators);
    Flux<Validator> findAll();
    Mono<PageResponse<ValidatorSummaryDto>> findAll(ValidatorFilter filter);
}
