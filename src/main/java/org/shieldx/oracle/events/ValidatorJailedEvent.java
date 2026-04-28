package org.shieldx.oracle.events;

import org.shieldx.oracle.entity.Validator;

import java.time.Instant;

public record ValidatorJailedEvent(
        Validator validator,
        Instant occurredAt
) implements DomainEvent {
}
