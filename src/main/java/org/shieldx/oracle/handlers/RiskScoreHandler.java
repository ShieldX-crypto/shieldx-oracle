package org.shieldx.oracle.handlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shieldx.oracle.events.DomainEvent;
import org.shieldx.oracle.events.ValidatorMetricsChangedEvent;
import org.shieldx.oracle.service.RiskService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class RiskScoreHandler implements DomainEventHandler<ValidatorMetricsChangedEvent> {
    private final RiskService riskService;

    @Override
    public boolean supports(DomainEvent event) {
        return event instanceof ValidatorMetricsChangedEvent;
    }

    @Override
    public Mono<Void> handle(ValidatorMetricsChangedEvent event) {
        log.trace("Recalculating risk score for validator: {}", event.after().getOwner());
        return riskService
                .calculate(event.after().getOwner())
                .doOnSuccess(risk -> log.debug("Recalculated risk score: {}", risk))
                .then();
    }
}
