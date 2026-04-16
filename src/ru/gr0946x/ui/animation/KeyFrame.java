package ru.gr0946x.ui.animation;

public class KeyFrame {
    public final double xMin, xMax, yMin, yMax;
    public final double duration;

    public KeyFrame(double xMin, double xMax, double yMin, double yMax, double duration) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        this.duration = duration;
    }
}