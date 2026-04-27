package org.shieldx.oracle.service;

import org.shieldx.oracle.entity.Validator;
import reactor.core.publisher.Mono;

public interface RiskService {
    Mono<Void> recalculate(Validator validator);
}
