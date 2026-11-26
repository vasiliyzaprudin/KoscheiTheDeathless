package com.mining.koscheithedeathless.design.devclassification.dto;

import lombok.Data;

@Data
public class PropertyDto {
    private Long propertyStabilityId;
    private String propertyStabilityNameRu;
    private String unit;
    private boolean isRequired;
}