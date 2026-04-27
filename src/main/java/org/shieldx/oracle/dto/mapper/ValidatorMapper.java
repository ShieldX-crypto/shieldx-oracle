package org.shieldx.oracle.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.shieldx.oracle.dto.validator.ValidatorDto;
import org.shieldx.oracle.entity.Validator;
import org.shieldx.oracle.entity.ValidatorListStatus;

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
    Validator toEntity(ValidatorDto validatorDto);

    List<Validator> toEntity(List<ValidatorDto> validatorDtoList);

    @Named("statusMapper")
    default ValidatorListStatus map(String value) {
        return ValidatorListStatus.from(value);
    }
}
