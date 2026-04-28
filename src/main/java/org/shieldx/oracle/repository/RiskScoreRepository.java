package org.shieldx.oracle.repository;

import org.shieldx.oracle.entity.RiskScore;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Repository
public interface RiskScoreRepository extends ReactiveCrudRepository<RiskScore, Long> {
    Mono<RiskScore> findTopByValidatorOwnerOrderByCalculatedAtDesc(String validatorOwner);

    Flux<RiskScore> findAllByValidatorOwnerOrderByCalculatedAtDesc(String validatorOwner);

    Flux<RiskScore> findAllByValidatorOwnerAndCalculatedAtAfterOrderByCalculatedAtAsc(
            String validatorOwner,
            Instant from
    );
}
