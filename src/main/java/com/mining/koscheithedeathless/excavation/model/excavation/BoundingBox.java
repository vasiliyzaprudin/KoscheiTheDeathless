package com.mining.koscheithedeathless.excavation.model.excavation;

public record BoundingBox(double minX, double minY, double maxX, double maxY) {
    public double width()  { return maxX - minX; }
    public double height() { return maxY - minY; }
}