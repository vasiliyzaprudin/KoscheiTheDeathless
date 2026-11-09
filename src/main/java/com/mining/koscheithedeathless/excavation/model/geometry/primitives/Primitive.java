package com.mining.koscheithedeathless.excavation.model.geometry.primitives;

import com.mining.koscheithedeathless.excavation.model.excavation.Style;

public sealed interface Primitive permits Line, Arc {
    String id();
    String role();
    Style style();
}
