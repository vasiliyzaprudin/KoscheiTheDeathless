package com.mining.koscheithedeathless.model.excavation;

import java.util.List;
import java.util.Map;

public record DrawingModel(
        String title,
        BoundingBox bounds,
        List<Primitive> primitives,
        Map<String, Object> meta
) {}