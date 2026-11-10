package com.mining.koscheithedeathless.excavation.dto;

import lombok.Data;

@Data
public class ReportRequest {
    private double width;
    private double wallHeight;
    private String description;
    private String svgContent; // Сюда клиент пришлет готовый SVG-код
}
