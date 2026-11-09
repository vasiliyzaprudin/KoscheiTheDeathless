package com.mining.koscheithedeathless.excavation.model.excavation;

public record RenderOptions(
        double scale,
        double padding,
        int imageWidth,
        int imageHeight,
        double offsetX,
        double offsetY
) {

    /**
     * Вписывает модель в холст фиксированного размера maxWidth × maxHeight.
     * Отступы padding одинаковы по всем четырём краям.
     * Если соотношение сторон модели не совпадает с холстом —
     * модель центрируется по свободной оси.
     */
    public static RenderOptions fitTo(DrawingModel model, int maxWidth, int maxHeight) {
        BoundingBox b = model.bounds();
        double padding = 40;

        double availableW = maxWidth  - 2 * padding;
        double availableH = maxHeight - 2 * padding;

        double scale = Math.min(
                availableW / b.width(),
                availableH / b.height()
        );

        // Реальный размер модели в пикселях после масштабирования
        double drawnW = b.width()  * scale;
        double drawnH = b.height() * scale;

        // Центрируем модель в доступной области
        double offsetX = (availableW - drawnW) / 2.0;
        double offsetY = (availableH - drawnH) / 2.0;

        return new RenderOptions(
                scale,
                padding,
                maxWidth,
                maxHeight,
                offsetX,
                offsetY
        );
    }

    /** Метры → SVG X */
    public double toSvgX(double modelX, BoundingBox b) {
        return padding + offsetX + (modelX - b.minX()) * scale;
    }

    /** Метры → SVG Y (инверсия: в SVG Y растёт вниз) */
    public double toSvgY(double modelY, BoundingBox b) {
        return padding + offsetY + (b.maxY() - modelY) * scale;
    }
}