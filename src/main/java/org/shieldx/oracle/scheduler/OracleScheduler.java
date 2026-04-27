package org.shieldx.oracle.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shieldx.oracle.dto.mapper.ValidatorMapper;
import org.shieldx.oracle.entity.Validator;
import org.shieldx.oracle.integration.ValidatorApiClient;
import org.shieldx.oracle.service.ValidatorService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OracleScheduler {
    private final ValidatorApiClient validatorApiClient;
    private final ValidatorService validatorService;
    private final ValidatorMapper validatorMapper;

    @Scheduled(cron = "*/30 * * * * *")
    public void fetchAllValidators() {
        log.info("Fetching all validators");
        validatorApiClient
                .fetchAllValidators()
                .buffer(50)
                .flatMap(validators -> {
                    log.debug("Received {} validators", validators.size());
                    List<Validator> mapped = validatorMapper.toEntity(validators);
                    return validatorService.saveBatch(mapped)
                            .doOnTerminate(() -> log.debug("Saved {} validators", mapped.size()));
                }).subscribe(
                        null,
                        err -> log.error("Failed to save validators", err)
                );
    }
}
