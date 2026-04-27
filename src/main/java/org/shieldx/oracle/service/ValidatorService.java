package org.shieldx.oracle.service;

import org.shieldx.oracle.entity.Validator;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ValidatorService {
    Mono<Void> saveBatch(List<Validator> validators);
}
