package com.mining.koscheithedeathless.model.excavation;

public record Arc(String id, String role, Style style,
                  Point center, double radius,
                  double startAngleDeg, double endAngleDeg)
        implements Primitive {
}
