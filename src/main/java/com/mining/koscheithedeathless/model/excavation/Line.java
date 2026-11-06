package com.mining.koscheithedeathless.model.excavation;

public record Line(
        String id, String role, Style style,
        Point start, Point end
) implements Primitive {}