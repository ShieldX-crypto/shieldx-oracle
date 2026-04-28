package org.shieldx.oracle.repository;

import org.shieldx.oracle.entity.JailEvent;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface JailEventRepository extends ReactiveCrudRepository<JailEvent, Long> {
    Flux<JailEvent> findAllByValidatorOwner(String validatorOwner);

    Mono<Boolean> existsByValidatorOwner(String validatorOwner);
}
