package com.mining.koscheithedeathless.model.excavation;

public record Style(
        String stroke,
        Double strokeWidth,
        String fill,
        String strokeDasharray
) {
    public static final Style DEFAULT  = new Style("#000000", 1.0, "none", null);
    public static final Style CONTOUR = new Style("#1a1a1a", 1.0, "none", null);
    public static final Style DASHED  = new Style("#888888", 1.0, "none", "6,4");
}