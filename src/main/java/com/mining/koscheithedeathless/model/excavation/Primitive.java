package com.mining.koscheithedeathless.model.excavation;

public sealed interface Primitive permits Line, Arc {
    String id();
    String role();
    Style style();
}
