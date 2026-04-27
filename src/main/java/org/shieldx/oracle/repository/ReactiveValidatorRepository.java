package org.shieldx.oracle.repository;

import org.shieldx.oracle.entity.Validator;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;

@Repository
public interface ReactiveValidatorRepository extends ReactiveCrudRepository<Validator, String> {
    Flux<Validator> findAllByOwnerIn(List<String> owners);
}
