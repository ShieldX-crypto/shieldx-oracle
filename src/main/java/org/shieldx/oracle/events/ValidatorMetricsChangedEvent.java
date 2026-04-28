package org.shieldx.oracle.events;

import org.shieldx.oracle.entity.Validator;

import java.time.Instant;

public record ValidatorMetricsChangedEvent(
        Validator before,
        Validator after,
        Instant occurredAt
) implements DomainEvent {
}
