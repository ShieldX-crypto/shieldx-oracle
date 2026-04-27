package org.shieldx.oracle.infrastructure;

import org.shieldx.oracle.events.DomainEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Component
public class DomainEventPublisher {
    public static final int BACKPRESSURE_BUFFER_SIZE = 1024;

    private final Sinks.Many<DomainEvent> sink = Sinks.many()
            .multicast()
            .onBackpressureBuffer(BACKPRESSURE_BUFFER_SIZE);

    public void publish(DomainEvent event) {
        sink.tryEmitNext(event);
    }

    public Flux<DomainEvent> asFlux() {
        return sink.asFlux();
    }

    public Mono<Void> publishReactive(DomainEvent event) {
        return Mono.fromRunnable(() -> publish(event));
    }
}
