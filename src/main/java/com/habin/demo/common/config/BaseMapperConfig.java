package com.habin.demo.common.config;

import org.mapstruct.*;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValueMappingStrategy.RETURN_NULL;
import static org.mapstruct.ReportingPolicy.IGNORE;

@MapperConfig(
        componentModel = SPRING,
        unmappedTargetPolicy = IGNORE,
        nullValueMappingStrategy = RETURN_NULL,
        nullValueCheckStrategy = ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface BaseMapperConfig {
}
