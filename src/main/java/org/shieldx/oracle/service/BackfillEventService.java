package org.shieldx.oracle.service;

import reactor.core.publisher.Mono;

public interface BackfillEventService {
    Mono<Void> backfillAll();
}
