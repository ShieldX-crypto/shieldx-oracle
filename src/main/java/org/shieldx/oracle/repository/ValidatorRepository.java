package org.shieldx.oracle.repository;

import org.shieldx.oracle.entity.Validator;
import org.shieldx.oracle.repository.projection.ValidatorDetailProjection;
import org.shieldx.oracle.repository.projection.ValidatorSummaryProjection;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public interface ValidatorRepository extends ReactiveCrudRepository<Validator, String> {
    Flux<Validator> findAllByOwnerIn(List<String> owners);

    Mono<Validator> findByOwner(String owner);

    @Query("""
        SELECT
            v.owner,
            v.name,
            v.status,
            v.jailed,
            v.commission,
            v.total_stake,
            v.max_delegation,
            rs.total_score      AS risk_score,
            rs.model_version    AS risk_model_version
        FROM validators v
        LEFT JOIN risk_scores rs ON rs.validator_owner = v.owner
            AND rs.calculated_at = (
                SELECT MAX(r.calculated_at)
                FROM risk_scores r
                WHERE r.validator_owner = v.owner
            )
        WHERE v.owner IN (:owners)
    """)
    Flux<ValidatorSummaryProjection> findSummariesByOwnerIn(List<String> owners);

    @Query("""
        SELECT
            v.*,
            rs.jail_score,
            rs.uptime_score,
            rs.skin_score,
            rs.total_score      AS risk_score,
            rs.tier             AS risk_tier,
            rs.model_version    AS risk_model_version,
            rs.calculated_at    AS risk_calculated_at
        FROM validators v
        LEFT JOIN risk_scores rs ON rs.validator_owner = v.owner
            AND rs.calculated_at = (
                SELECT MAX(r.calculated_at)
                FROM risk_scores r
                WHERE r.validator_owner = v.owner
            )
        WHERE v.owner = :owner
    """)
    Mono<ValidatorDetailProjection> findDetailByOwner(String owner);
}
