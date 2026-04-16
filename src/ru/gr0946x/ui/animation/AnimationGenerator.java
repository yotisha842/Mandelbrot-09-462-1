package ru.gr0946x.ui.animation;

import java.util.ArrayList;
import java.util.List;

public class AnimationGenerator {

    /**
     * Генерирует последовательность кадров для анимации между ключевыми кадрами
     * @param keys список ключевых кадров
     * @param fps кадров в секунду
     * @return список сгенерированных кадров (включая ключевые)
     */
    public static List<KeyFrame> generate(List<KeyFrame> keys, int fps) {
        List<KeyFrame> frames = new ArrayList<>();

        if (keys == null || keys.size() < 2) {
            return frames;
        }

        for (int i = 0; i < keys.size() - 1; i++) {
            KeyFrame from = keys.get(i);
            KeyFrame to = keys.get(i + 1);

            // Количество кадров для этого сегмента
            // duration - это время до следующего кадра (в секундах)
            int frameCount = (int) Math.round(from.duration * fps);

            // Добавляем начальный кадр (только для первого сегмента)
            if (i == 0) {
                frames.add(from);
            }

            // Генерируем промежуточные кадры
            for (int j = 1; j <= frameCount; j++) {
                double t = (double) j / frameCount;
                KeyFrame interpolated = FrameInterpolator.interpolate(from, to, t);
                frames.add(interpolated);
            }
        }

        // Добавляем последний ключевой кадр, если его еще нет
        KeyFrame lastKey = keys.get(keys.size() - 1);
        if (frames.isEmpty() || !frames.get(frames.size() - 1).equals(lastKey)) {
            frames.add(lastKey);
        }

        return frames;
    }

    /**
     * Рассчитывает общую длительность анимации в секундах
     */
    public static double calculateTotalDuration(List<KeyFrame> keys) {
        if (keys == null || keys.isEmpty()) {
            return 0;
        }

        double total = 0;
        // Суммируем длительности всех ключевых кадров, кроме последнего
        for (int i = 0; i < keys.size() - 1; i++) {
            total += keys.get(i).duration;
        }
        return total;
    }

    /**
     * Рассчитывает фактическое количество кадров для анимации
     */
    public static int calculateTotalFrames(List<KeyFrame> keys, int fps) {
        if (keys == null || keys.size() < 2) {
            return 0;
        }

        int totalFrames = 1; // первый кадр
        for (int i = 0; i < keys.size() - 1; i++) {
            totalFrames += (int) Math.round(keys.get(i).duration * fps);
        }
        return totalFrames;
    }
}