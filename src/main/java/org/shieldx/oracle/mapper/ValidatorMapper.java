package org.shieldx.oracle.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.shieldx.oracle.api.dto.validator.ValidatorDetailDto;
import org.shieldx.oracle.api.dto.validator.ValidatorSummaryDto;
import org.shieldx.oracle.entity.Validator;
import org.shieldx.oracle.entity.ValidatorListStatus;
import org.shieldx.oracle.integration.dto.validator.ValidatorDto;
import org.shieldx.oracle.repository.projection.ValidatorDetailProjection;
import org.shieldx.oracle.repository.projection.ValidatorSummaryProjection;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ValidatorMapper {

    @Mapping(source = "ownerAddress", target = "owner")
    @Mapping(source = "list", target = "status", qualifiedByName = "statusMapper")
    @Mapping(source = "totalValidatorSuccessRate.numSuccess", target = "totalValidatorSuccess")
    @Mapping(source = "totalValidatorSuccessRate.numFailure", target = "totalValidatorFailure")
    @Mapping(source = "totalLeaderSuccessRate.numSuccess", target = "totalLeaderSuccess")
    @Mapping(source = "totalLeaderSuccessRate.numFailure", target = "totalLeaderFailure")
    @Mapping(source = "totalValidatorIgnoredSignaturesRate", target = "totalSignaturesIgnored")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Validator toEntity(ValidatorDto validatorDto);

    List<Validator> toEntity(List<ValidatorDto> validatorDtoList);


    @Mapping(target = "remainingCapacity",
            expression = "java(Math.max(0, p.getMaxDelegation() - p.getTotalStake()))")
    @Mapping(target = "status",
            expression = "java(ValidatorListStatus.valueOf(p.getStatus()))")
    ValidatorSummaryDto toSummaryDto(ValidatorSummaryProjection p);

    @Mapping(target = "remainingCapacity",
            expression = "java(Math.max(0, p.getMaxDelegation() - p.getTotalStake()))")
    @Mapping(target = "status",
            expression = "java(ValidatorListStatus.valueOf(p.getStatus()))")
    ValidatorDetailDto toDetailDto(ValidatorDetailProjection p);

    @Named("statusMapper")
    default ValidatorListStatus map(String value) {
        return ValidatorListStatus.from(value);
    }
}
