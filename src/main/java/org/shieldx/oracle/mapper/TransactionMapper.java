package org.shieldx.oracle.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.shieldx.oracle.entity.JailEvent;
import org.shieldx.oracle.integration.dto.transaction.TransactionDto;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    @Mapping(source = "sender", target = "validatorOwner")
    @Mapping(source = "timestamp", target = "detectedAt", qualifiedByName = "toInstant")
    @Mapping(target = "id", ignore = true)
    JailEvent toEntity(TransactionDto dto);

    @Named("toInstant")
    default Instant toInstant(Long timestamp) {
        return timestamp != null ? Instant.ofEpochSecond(timestamp) : null;
    }
}
