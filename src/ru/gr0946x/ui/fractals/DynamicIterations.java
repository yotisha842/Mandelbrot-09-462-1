package ru.gr0946x.ui.fractals;

public class DynamicIterations {
    private static final double BASE_ITERATIONS = 100.0;
    private static final double REFERENCE_WIDTH = 3.0;
    private static final double ZOOM_FACTOR = 1.25;
    private static final int MIN_ITERATIONS = 50;
    private static final int MAX_ITERATIONS = 5000;

    private boolean enabled = false;
    private double lastWidth = REFERENCE_WIDTH;

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int updateAndGetIterations(double width) {
        this.lastWidth = width;
        return calculateIterations(width);
    }

    public int getCurrentIterations() {
        return calculateIterations(lastWidth);
    }

    private int calculateIterations(double width) {
        if (!enabled) {
            return 100;
        }
        if (width <= 0) {
            return MAX_ITERATIONS;
        }

        double zoomLevel = Math.log(REFERENCE_WIDTH / width) / Math.log(2);
        int result = (int) Math.round(BASE_ITERATIONS * Math.pow(ZOOM_FACTOR, zoomLevel));

        result = Math.max(MIN_ITERATIONS, result);
        result = Math.min(MAX_ITERATIONS, result);
        return result;
    }

    public void setLastWidth(double width) {
        this.lastWidth = width;
    }

    public void syncLastWidth(double width) {
        this.lastWidth = width;
    }
}