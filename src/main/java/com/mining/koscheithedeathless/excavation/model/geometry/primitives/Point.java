package com.mining.koscheithedeathless.excavation.model.geometry.primitives;

public record Point(double x, double y) {
    public static final Point ZERO = new Point(0, 0);
}
