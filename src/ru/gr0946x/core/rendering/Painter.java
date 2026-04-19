package ru.gr0946x.core.rendering;

import java.awt.*;

/**
 * Контракт для компонентов, способных отрисовывать себя на Graphics-контексте.
 * Используется для абстракции отрисовки фрактала, позволяет подменять реализацию
 * (например, FractalPainter, JuliaPainter) без изменения кода отображения.
 */
public interface Painter {
    int getWidth();
    int getHeight();

    void setWidth(int width);
    void setHeight(int height);

    void paint(Graphics g);
}
