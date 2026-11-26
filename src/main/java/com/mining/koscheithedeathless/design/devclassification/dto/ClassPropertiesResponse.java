package com.mining.koscheithedeathless.design.devclassification.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class ClassPropertiesResponse {
    private Long geologicalStabilityClassMineId;
    private String geologicalStabilityClassMineCode;
    private String globalClassCode;
    private Long mineId;
    private List<PropertyDto> properties;
    private Map<Long, List<RangeValueDto>> availableValues; // ключ — propertyStabilityId
    private List<SavedValueDto> savedValues;
}