package com.mining.koscheithedeathless.excavation.model.geometry.primitives;

import com.mining.koscheithedeathless.excavation.model.excavation.Style;

public record Arc(String id, String role, Style style,
                  Point center, double radius,
                  double startAngleDeg, double endAngleDeg)
        implements Primitive {
}
