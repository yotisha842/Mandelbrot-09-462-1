package ru.gr0946x.ui.animation;

/**
 * Утилитарный класс для линейной интерполяции (lerp) между ключевыми кадрами.
 * Используется AnimationGenerator для создания промежуточных кадров анимации.
 */
public class FrameInterpolator {

    public static KeyFrame interpolate(KeyFrame a, KeyFrame b, double t) {
        return new KeyFrame(
                lerp(a.xMin, b.xMin, t),
                lerp(a.xMax, b.xMax, t),
                lerp(a.yMin, b.yMin, t),
                lerp(a.yMax, b.yMax, t),
                0
        );
    }

    private static double lerp(double a, double b, double t) {
        return a + (b - a) * t;
    }
}