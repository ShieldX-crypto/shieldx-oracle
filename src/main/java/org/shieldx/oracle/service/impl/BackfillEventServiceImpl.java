package org.shieldx.oracle.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shieldx.oracle.integration.KleverApiClient;
import org.shieldx.oracle.mapper.TransactionMapper;
import org.shieldx.oracle.repository.JailEventRepository;
import org.shieldx.oracle.service.BackfillEventService;
import org.shieldx.oracle.service.ValidatorService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class BackfillEventServiceImpl implements BackfillEventService {
    private final KleverApiClient kleverApiClient;
    private final JailEventRepository jailRepository;
    private final ValidatorService validatorService;
    private final TransactionMapper transactionMapper;

    public Mono<Void> backfillAll() {
        return validatorService.findAll()
                .flatMap(v -> backfillForValidator(v.getOwner())
                        .onErrorResume(e -> {
                            log.warn("Backfill failed for {}", v.getOwner(), e);
                            return Mono.empty();
                        })
                )
                .then();
    }

    private Mono<Void> backfillForValidator(String owner) {
        return jailRepository.existsByValidatorOwner(owner)
                .flatMap(alreadyBackfilled -> {
                    if (alreadyBackfilled) {
                        log.debug("Skipping backfill for {} — already done", owner);
                        return Mono.empty();
                    }
                    return fetchAndSaveUnjailTransactions(owner);
                });
    }

    private Mono<Void> fetchAndSaveUnjailTransactions(String owner) {
        return kleverApiClient.fetchAllUnjailsByValidator(owner)
                .map(transactionMapper::toEntity)
                .collectList()
                .flatMap(events -> {
                    if (events.isEmpty()) return Mono.empty();
                    log.info("Backfilling {} jail events for {}", events.size(), owner);
                    return jailRepository.saveAll(events).then();
                });
    }
}
