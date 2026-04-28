package org.shieldx.oracle.service;

import org.shieldx.oracle.entity.RiskScore;
import org.shieldx.oracle.entity.Validator;
import reactor.core.publisher.Mono;

public interface RiskService {
    Mono<RiskScore> calculate(String owner);
}
