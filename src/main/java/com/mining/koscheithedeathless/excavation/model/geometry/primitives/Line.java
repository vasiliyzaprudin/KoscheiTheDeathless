package com.mining.koscheithedeathless.excavation.model.geometry.primitives;

import com.mining.koscheithedeathless.excavation.model.excavation.Style;

public record Line(
        String id, String role, Style style,
        Point start, Point end
) implements Primitive {}