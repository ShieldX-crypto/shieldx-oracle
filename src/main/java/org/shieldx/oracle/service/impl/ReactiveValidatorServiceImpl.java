package org.shieldx.oracle.service.impl;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shieldx.oracle.entity.Validator;
import org.shieldx.oracle.events.DomainEvent;
import org.shieldx.oracle.events.ValidatorJailedEvent;
import org.shieldx.oracle.events.ValidatorMetricsChangedEvent;
import org.shieldx.oracle.events.ValidatorUnjailedEvent;
import org.shieldx.oracle.infrastructure.DomainEventPublisher;
import org.shieldx.oracle.repository.ReactiveValidatorRepository;
import org.shieldx.oracle.service.ValidatorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReactiveValidatorServiceImpl implements ValidatorService {
    private final ReactiveValidatorRepository validatorRepository;
    private final TransactionalOperator tx;
    private final DomainEventPublisher publisher;

    public Mono<Void> saveBatch(List<Validator> incoming) {
        List<String> owners = incoming.stream()
                .map(Validator::getOwner)
                .toList();

        return validatorRepository.findAllByOwnerIn(owners)
                .collectMap(Validator::getOwner)
                .flatMap(existingMap ->
                        tx.transactional(
                                Flux.fromIterable(incoming)
                                        .flatMap(v -> processOne(v, existingMap.get(v.getOwner())))
                                        .then()
                        )
                );
    }



    private Mono<Void> processOne(Validator incoming, @Nullable Validator existing) {
        if (existing == null) {
            return validatorRepository.save(incoming)
                    .flatMap((saved) -> {
                        log.trace("Created validator record {}. ID: {}", incoming.getOwner(), saved.getId());
                        return Mono.empty();
                    }).then();
        }

        incoming.setId(existing.getId());

        if (incoming.equals(existing)) {
            log.trace("Validator record not changed. ID: {}", incoming.getOwner());
            return Mono.empty();
        }

        return validatorRepository.save(incoming)
                .flatMap(saved -> {
                    log.trace("Updated validator record {}. ID: {}", incoming.getOwner(), saved.getId());
                    return handleChanges(existing, saved);
                })
                .then();
    }

    private Mono<Void> handleChanges(Validator before, Validator after) {
        List<DomainEvent> events = new ArrayList<>();

        if (!before.isJailed() && after.isJailed()) {
            events.add(new ValidatorJailedEvent(after, Instant.now()));
        }

        if (before.isJailed() && !after.isJailed()) {
            events.add(new ValidatorUnjailedEvent(after, Instant.now()));
        }

        if (before.getTotalValidatorFailure() != after.getTotalValidatorFailure()
                || before.getNumJailed() != after.getNumJailed()) {
            events.add(new ValidatorMetricsChangedEvent(before, after, Instant.now()));
        }

        return Flux.fromIterable(events)
                .flatMap(publisher::publishReactive)
                .then();
    }
}
