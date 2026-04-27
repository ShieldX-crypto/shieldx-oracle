package org.shieldx.oracle.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.shieldx.oracle.dto.validator.ValidatorDto;
import org.shieldx.oracle.entity.Validator;

@Mapper(componentModel = "spring")
public interface ValidatorMapper {

    //@Mapping(source = "")
    Validator toEntity(ValidatorDto validatorDto);
}
