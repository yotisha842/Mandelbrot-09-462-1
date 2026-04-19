package ru.gr0946x.core.fractals;

import ru.smak.math.Complex;

/**
 * Реализует множество Жюлиа, где параметр c фиксирован, а начальное z — координата пикселя.
 * Наследует логику проверки лимита от родителя, меняя только начальные условия.
 */
public class Julia extends Mandelbrot {
    private final Complex c;

    public Julia(Complex c) {
        this.c = c;
    }

    public void setDynamicIterations(DynamicIterations di) {
        super.setDynamicIterations(di);
    }

    // Вычисляет долю итераций для точки, используя фиксированную константу c из конструктора.
    @Override
    public float inSetProbability(double x, double y) {
        var z = new Complex(x, y);

        int currentMax = getCurrentMaxIterations();

        int i = 0;
        while (z.getAbsoluteValue2() < 4 && ++i < currentMax) {
            z.timesAssign(z);
            z.plusAssign(c);
        }
        return (float) i / currentMax;
    }
}