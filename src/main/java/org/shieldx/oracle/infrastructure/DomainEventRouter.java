package org.shieldx.oracle.infrastructure;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shieldx.oracle.events.DomainEvent;
import org.shieldx.oracle.handlers.DomainEventHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DomainEventRouter {
    private final DomainEventPublisher publisher;
    private final List<DomainEventHandler<? extends DomainEvent>> handlers;

    @PostConstruct
    public void init() {
        publisher.asFlux()
                .flatMap(this::route)
                .onErrorContinue((err, event) -> {
                    log.error("Handler failed for event {}", event, err);
                })
                .subscribe();
    }

    @SuppressWarnings("unchecked")
    private Mono<Void> route(DomainEvent event) {
        return Flux.fromIterable(handlers)
                .filter(h -> h.supports(event))
                .flatMap(h -> ((DomainEventHandler<DomainEvent>) h).handle(event))
                .then();
    }
}
