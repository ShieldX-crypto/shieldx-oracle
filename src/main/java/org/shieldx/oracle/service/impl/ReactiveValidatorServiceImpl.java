package org.shieldx.oracle.service.impl;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shieldx.oracle.api.dto.PageResponse;
import org.shieldx.oracle.api.dto.validator.ValidatorFilter;
import org.shieldx.oracle.api.dto.validator.ValidatorSummaryDto;
import org.shieldx.oracle.entity.Validator;
import org.shieldx.oracle.events.DomainEvent;
import org.shieldx.oracle.events.ValidatorJailedEvent;
import org.shieldx.oracle.events.ValidatorMetricsChangedEvent;
import org.shieldx.oracle.events.ValidatorUnjailedEvent;
import org.shieldx.oracle.infrastructure.DomainEventPublisher;
import org.shieldx.oracle.mapper.ValidatorMapper;
import org.shieldx.oracle.repository.ValidatorRepository;
import org.shieldx.oracle.service.ValidatorService;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
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
    private final ValidatorRepository validatorRepository;
    private final TransactionalOperator tx;
    private final DomainEventPublisher publisher;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final ValidatorMapper validatorMapper;

    @Override
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

    @Override
    public Flux<Validator> findAll() {
        return validatorRepository.findAll();
    }

    @Override
    public Mono<PageResponse<ValidatorSummaryDto>> findAll(ValidatorFilter filter) {
        Criteria criteria = buildCriteria(filter);

        Query query = Query.query(criteria)
                .sort(buildSort(filter))
                .limit(filter.getSize())
                .offset((long) filter.getPage() * filter.getSize());

        Mono<Long> count = r2dbcEntityTemplate
                .count(Query.query(criteria), Validator.class);

        Flux<ValidatorSummaryDto> content = r2dbcEntityTemplate
                .select(Validator.class)
                .matching(query)
                .all()
                .map(Validator::getOwner)
                .collectList()
                .flatMapMany(owners -> {
                    if (owners.isEmpty()) {
                        return Flux.empty();
                    } else {
                        return validatorRepository.findSummariesByOwnerIn(owners);
                    }
                })
                .map(validatorMapper::toSummaryDto);

        return Mono.zip(content.collectList(), count)
                .map(tuple -> PageResponse.of(tuple.getT1(), tuple.getT2(), filter));
    }

    private Criteria buildCriteria(ValidatorFilter filter) {
        Criteria criteria = Criteria.empty();
        if (filter.getJailed() != null) {
            criteria = criteria.and("jailed").is(filter.getJailed());
        }
        if (filter.getStatus() != null) {
            criteria = criteria.and("status").is(filter.getStatus());
        }
        if (filter.getMaxCommission() != null) {
            criteria = criteria.and("commission").lessThanOrEquals(filter.getMaxCommission());
        }
        return criteria;
    }

    private Sort buildSort(ValidatorFilter filter) {
        String column = switch (filter.getSortBy()) {
            case TOTAL_STAKE -> "totalStake";
            case SELF_STAKE -> "selfStake";
            case COMMISSION -> "commission";
            case NUM_JAILED -> "numJailed";
            case TOTAL_VALIDATOR_SUCCESS -> "totalValidatorSuccess";
            case RISK_SCORE -> "totalStake"; // risk_score не в таблице validators — fallback
        };
        return Sort.by(
                filter.getDirection() == ValidatorFilter.SortDirection.ASC
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC,
                column
        );
    }
}
