package org.shieldx.oracle.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shieldx.oracle.integration.ValidatorApiClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OracleScheduler {
    private final ValidatorApiClient validatorApiClient;

    @Scheduled(cron = "*/5 * * * * *")
    public void fetchAllValidators() {
        log.info("Fetching all validators");
        var result = validatorApiClient.fetchValidator("klv109wywwukn2xl2egqwnqsz7884hrdsk5jn4s859080hwcf6rs583swkuzf8").block();
        log.info(result.toString());
    }
}
