package org.shieldx.oracle.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shieldx.oracle.service.BackfillEventService;
import org.shieldx.oracle.service.RiskService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnBooleanProperty("oracle.scheduling.enabled")
public class BackfillRunner implements ApplicationListener<ApplicationReadyEvent> {

    private final BackfillEventService backfillService;
    private final RiskService riskService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        backfillService.backfillAll()
                .doOnSuccess(v -> log.info("Backfill completed"))
                .doOnError(e -> log.error("Backfill failed", e))
                .subscribe();
    }
}