package ru.gr0946x.ui.animation;

import java.util.ArrayList;
import java.util.List;

/**
 * Генератор кадров анимации по ключевым точкам.
 * Принимает список ключевых кадров (KeyFrame) и частоту кадров (fps),
 * возвращает полный список кадров с плавной интерполяцией между ключами.
 */
public class AnimationGenerator {

    // Создаёт последовательность кадров для анимации между ключевыми точками.
    public static List<KeyFrame> generate(List<KeyFrame> keys, int fps) {
        List<KeyFrame> frames = new ArrayList<>();

        if (keys == null || keys.size() < 2) {
            return frames;
        }

        for (int i = 0; i < keys.size() - 1; i++) {
            KeyFrame from = keys.get(i);
            KeyFrame to = keys.get(i + 1);

            int frameCount = (int) Math.round(from.duration * fps);

            if (i == 0) {
                frames.add(from);
            }

            for (int j = 1; j <= frameCount; j++) {
                double t = (double) j / frameCount;
                KeyFrame interpolated = FrameInterpolator.interpolate(from, to, t);
                frames.add(interpolated);
            }
        }

        KeyFrame lastKey = keys.get(keys.size() - 1);
        if (frames.isEmpty() || !frames.get(frames.size() - 1).equals(lastKey)) {
            frames.add(lastKey);
        }

        return frames;
    }
}