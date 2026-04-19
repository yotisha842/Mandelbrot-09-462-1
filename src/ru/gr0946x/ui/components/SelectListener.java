package ru.gr0946x.ui.components;

import java.awt.*;

/**
 * Функциональный интерфейс для обработки события выбора области мышью.
 * Вызывается при завершении выделения прямоугольника (при отпускании кнопки мыши).
 */
@FunctionalInterface
public interface SelectListener {
    void onSelect(Rectangle rect);
}
