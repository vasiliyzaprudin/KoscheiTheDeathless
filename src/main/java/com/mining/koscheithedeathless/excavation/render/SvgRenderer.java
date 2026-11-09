package com.mining.koscheithedeathless.excavation.render;

import com.mining.koscheithedeathless.excavation.model.excavation.BoundingBox;
import com.mining.koscheithedeathless.excavation.model.excavation.DrawingModel;
import com.mining.koscheithedeathless.excavation.model.excavation.RenderOptions;
import com.mining.koscheithedeathless.excavation.model.excavation.Style;
import com.mining.koscheithedeathless.excavation.model.geometry.primitives.Arc;
import com.mining.koscheithedeathless.excavation.model.geometry.primitives.Line;
import com.mining.koscheithedeathless.excavation.model.geometry.primitives.Primitive;
import org.springframework.stereotype.Component;
import java.util.Locale;

@Component
public class SvgRenderer {

    public String render(DrawingModel model, RenderOptions options) {
        StringBuilder sb = new StringBuilder();
        BoundingBox b = model.bounds();

        sb.append(String.format(Locale.ROOT,
                "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"%d\" height=\"%d\" viewBox=\"0 0 %d %d\" shape-rendering=\"geometricPrecision\">\n",
                options.imageWidth(), options.imageHeight(),
                options.imageWidth(), options.imageHeight()));
        // Заливка фона
        sb.append(String.format(Locale.ROOT,
                "<rect x=\"0\" y=\"0\" width=\"%d\" height=\"%d\" fill=\"#eef4fb\"/>\n",
                options.imageWidth(), options.imageHeight()));
        for (Primitive p : model.primitives()) {
            switch (p) {
                case Line l -> sb.append(renderLine(l, b, options));
                case Arc a -> sb.append(renderArc(a, b, options));
            }
        }

        sb.append("</svg>");
        return sb.toString();
    }

    private String renderLine(Line l, BoundingBox b, RenderOptions o) {
        return String.format(Locale.ROOT, "  <line x1=\"%.2f\" y1=\"%.2f\" x2=\"%.2f\" y2=\"%.2f\" %s data-id=\"%s\" data-role=\"%s\"/>\n", o.toSvgX(l.start().x(), b), o.toSvgY(l.start().y(), b), o.toSvgX(l.end().x(), b), o.toSvgY(l.end().y(), b), styleAttrs(l.style()), l.id(), l.role());
    }

    private String renderArc(Arc a, BoundingBox b, RenderOptions o) {
        // Переводим углы из математической системы (CCW, Y вверх)
        // в SVG-координаты. Инверсия Y превращает CCW в CW, поэтому
        // используем sweep-flag = 0 и меняем местами начальный/конечный угол.
        double startRad = Math.toRadians(a.startAngleDeg());
        double endRad   = Math.toRadians(a.endAngleDeg());

        double x1 = a.center().x() + a.radius() * Math.cos(startRad);
        double y1 = a.center().y() + a.radius() * Math.sin(startRad);
        double x2 = a.center().x() + a.radius() * Math.cos(endRad);
        double y2 = a.center().y() + a.radius() * Math.sin(endRad);

        double sx = o.toSvgX(x1, b), sy = o.toSvgY(y1, b);
        double ex = o.toSvgX(x2, b), ey = o.toSvgY(y2, b);

        // large-arc-flag = 0 (угол < 180°), sweep-flag = 0 (CCW в SVG → CW на экране)
        String d = String.format(Locale.ROOT, "M %.2f %.2f A %.2f %.2f 0 0 0 %.2f %.2f",
                sx, sy,
                a.radius() * o.scale(), a.radius() * o.scale(),
                ex, ey);

        return String.format(Locale.ROOT,
                "  <path d=\"%s\" %s data-id=\"%s\" data-role=\"%s\"/>\n",
                d, styleAttrs(a.style()), a.id(), a.role());
    }

    private String styleAttrs(Style s) {
        StringBuilder sb = new StringBuilder();
        if (s.stroke() != null)
            sb.append("stroke=\"").append(s.stroke()).append("\" ");
        if (s.strokeWidth() != null)
            sb.append("stroke-width=\"").append(s.strokeWidth()).append("\" ");
        if (s.fill() != null)
            sb.append("fill=\"").append(s.fill()).append("\" ");
        if (s.strokeDasharray() != null)
            sb.append("stroke-dasharray=\"").append(s.strokeDasharray()).append("\" ");
        return sb.toString().trim();
    }
}
