package com.mining.koscheithedeathless.model.excavation;

public record Point(double x, double y) {
    public static final Point ZERO = new Point(0, 0);
}
