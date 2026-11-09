package com.mining.koscheithedeathless.excavation.service;

import com.mining.koscheithedeathless.excavation.model.excavation.BoundingBox;
import com.mining.koscheithedeathless.excavation.model.excavation.DrawingModel;
import com.mining.koscheithedeathless.excavation.model.excavation.Style;
import com.mining.koscheithedeathless.excavation.model.geometry.primitives.Arc;
import com.mining.koscheithedeathless.excavation.model.geometry.primitives.Line;
import com.mining.koscheithedeathless.excavation.model.geometry.primitives.Point;
import com.mining.koscheithedeathless.excavation.model.geometry.primitives.Primitive;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CrossSectionService {

    /**
     * Строит сечение: почва — горизонталь, две стенки — вертикали,
     * кровля — полуокружность радиусом width/2.
     *
     * Система координат: X вправо, Y вверх.
     * Почва — на y = 0, стены идут вверх до y = wallHeight,
     * кровля — дуга с центром (width/2, wallHeight), радиус width/2, углы 0..180.
     */
    public DrawingModel build(double width, double wallHeight) {
        if (width <= 0 || wallHeight <= 0) {
            throw new IllegalArgumentException("width и wallHeight должны быть > 0");
        }

        double r = width / 2.0;
        Point floorLeft  = new Point(0, 0);
        Point floorRight = new Point(width, 0);
        Point wallLeftTop  = new Point(0, wallHeight);
        Point wallRightTop = new Point(width, wallHeight);
        Point archCenter   = new Point(r, wallHeight);

        List<Primitive> primitives = new ArrayList<>();

        // Почва
        primitives.add(new Line(
                UUID.randomUUID().toString(), "floor", Style.CONTOUR,
                floorLeft, floorRight));

        // Левая стенка
        primitives.add(new Line(
                UUID.randomUUID().toString(), "wall-left", Style.CONTOUR,
                floorLeft, wallLeftTop));

        // Правая стенка
        primitives.add(new Line(
                UUID.randomUUID().toString(), "wall-right", Style.CONTOUR,
                floorRight, wallRightTop));

        // Кровля — полуокружность: от 0° (правая точка) до 180° (левая точка)
        primitives.add(new Arc(
                UUID.randomUUID().toString(), "roof", Style.CONTOUR,
                archCenter, r, 0, 180));

        // Осевая линия (вертикальная, пунктиром)
        double totalHeight = wallHeight + r;
        primitives.add(new Line(
                UUID.randomUUID().toString(), "axis", Style.DASHED,
                new Point(r, -0.1 * r), new Point(r, totalHeight + 0.1 * r)));

        BoundingBox bounds = new BoundingBox(
                -0.1 * r,
                -0.1 * r,
                width + 0.1 * r,
                totalHeight + 0.1 * r
        );

        return new DrawingModel(
                "Поперечное сечение выработки",
                bounds,
                primitives,
                Map.of("schemaVersion", "1.0", "units", "m")
        );
    }
}
