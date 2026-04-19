package ru.gr0946x.core.fractals;

import ru.smak.math.Complex;

/**
 * Реализует множество Мандельброта по формуле z = z² + c, где c — координата пикселя.
 * Считает шаги итерации до выхода точки за радиус 2 или достижения лимита.
 */
public class Mandelbrot implements Fractal {

    private int maxIterations = 100;
    private final double R2 = 4;

    private DynamicIterations dynamicIterations = null;

    public void setDynamicIterations(DynamicIterations di) {
        this.dynamicIterations = di;
    }

    // Возвращает актуальный лимит: динамический (если включён) или базовый (100).
    protected int getCurrentMaxIterations() {
        if (dynamicIterations != null && dynamicIterations.isEnabled()) {
            return dynamicIterations.getCurrentIterations();
        }
        return maxIterations;
    }

    // Запускает цикл для точки, считает итерации и возвращает их долю [0.0, 1.0].
    @Override
    public float inSetProbability(double x, double y) {
        var c = new Complex(x, y);
        var z = new Complex();

        int currentMax = getCurrentMaxIterations();

        int i = 0;
        while (z.getAbsoluteValue2() < R2 && ++i < currentMax){
            z.timesAssign(z);
            z.plusAssign(c);
        }
        return (float) i / currentMax;
    }
}