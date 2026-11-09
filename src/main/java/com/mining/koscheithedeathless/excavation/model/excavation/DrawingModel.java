package com.mining.koscheithedeathless.excavation.model.excavation;

import com.mining.koscheithedeathless.excavation.model.geometry.primitives.Primitive;

import java.util.List;
import java.util.Map;

public record DrawingModel(
        String title,
        BoundingBox bounds,
        List<Primitive> primitives,
        Map<String, Object> meta
) {}