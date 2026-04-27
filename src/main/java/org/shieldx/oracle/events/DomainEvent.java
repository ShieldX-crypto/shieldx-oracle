package org.shieldx.oracle.events;

public sealed interface DomainEvent
        permits ValidatorCreatedEvent, ValidatorJailedEvent, ValidatorUnjailedEvent, ValidatorMetricsChangedEvent {
}
