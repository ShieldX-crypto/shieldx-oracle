package org.shieldx.oracle.events;

import org.shieldx.oracle.entity.Validator;

import java.time.Instant;

public record ValidatorUnjailedEvent(
        Validator validator,
        Instant occurredAt
) implements DomainEvent {
}
