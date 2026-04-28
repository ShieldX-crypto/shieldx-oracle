package org.shieldx.oracle.handlers;

import org.shieldx.oracle.events.DomainEvent;
import reactor.core.publisher.Mono;

public interface DomainEventHandler<T extends DomainEvent> {
    boolean supports(DomainEvent event);
    Mono<Void> handle(T event);
}
