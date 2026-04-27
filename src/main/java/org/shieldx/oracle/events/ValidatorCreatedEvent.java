package org.shieldx.oracle.events;

import org.shieldx.oracle.entity.Validator;

public record ValidatorCreatedEvent(
        Validator validator
) implements DomainEvent {
}
