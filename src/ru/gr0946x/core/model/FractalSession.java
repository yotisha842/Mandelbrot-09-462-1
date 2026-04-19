package ru.gr0946x.core.model;

import ru.smak.math.Complex;
import java.io.Serializable;

/**
 * DTO для сохранения и восстановления состояния сессии фрактала.
 * Реализует Serializable для записи в файл .frac.
 * Хранит: тип фрактала, индексы настроек, границы области, параметр Жюлиа, настройки итераций.
 */
public class FractalSession implements Serializable {
    private static final long serialVersionUID = 1L;

    public String type;

    public int fractalIdx;

    public int colorIdx;

    public double xMin, xMax, yMin, yMax;

    public Double juliaCRe, juliaCIm;

    public boolean dynamicIterationsEnabled;
    public double dynamicIterationsLastWidth;

    public FractalSession() {}

    public FractalSession(String type, int fractalIdx, int colorIdx,
                          double xMin, double xMax, double yMin, double yMax,
                          Complex juliaC, boolean dynIterEnabled, double dynIterWidth) {
        this.type = type;
        this.fractalIdx = fractalIdx;
        this.colorIdx = colorIdx;
        this.xMin = xMin; this.xMax = xMax; this.yMin = yMin; this.yMax = yMax;
        if (juliaC != null) {
            this.juliaCRe = juliaC.getReal();
            this.juliaCIm = juliaC.getImaginary();
        }
        this.dynamicIterationsEnabled = dynIterEnabled;
        this.dynamicIterationsLastWidth = dynIterWidth;
    }
}