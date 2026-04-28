package org.shieldx.oracle.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Getter
@Setter
@Table(name = "jail_events")
public class JailEvent {
    @Id
    private Long id;
    private String validatorOwner;
    private Instant detectedAt;
}
