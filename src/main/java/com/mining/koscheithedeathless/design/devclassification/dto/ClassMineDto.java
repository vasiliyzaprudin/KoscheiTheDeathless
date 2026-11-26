package com.mining.koscheithedeathless.design.devclassification.dto;

import lombok.Data;

@Data
public class ClassMineDto {
    private Long geologicalStabilityClassMineId;
    private String geologicalStabilityClassMineCode;
    private Long globalClassId;
    private String globalClassCode;
    private Long mineId;
    private String mineName;
}