package org.shieldx.oracle.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.shieldx.oracle.entity.Validator;
import org.shieldx.oracle.service.RiskService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class ReactiveRiskServiceImpl implements RiskService {
    @Override
    public Mono<Void> recalculate(Validator validator) {
        return null;
    }
}
