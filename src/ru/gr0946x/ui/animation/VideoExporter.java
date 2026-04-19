package ru.gr0946x.ui.animation;

import org.jcodec.api.awt.AWTSequenceEncoder;
import ru.gr0946x.core.rendering.FractalPainter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

/**
 * Экспорт анимации фрактала в видеофайл (MP4) через jcodec.
 * Принимает список кадров, рендерит каждый через FractalPainter и кодирует в видео.
 */
public class VideoExporter {

    public static void export(
            List<KeyFrame> frames,
            int fps,
            FractalPainter painter,
            File file
    ) throws Exception {

        AWTSequenceEncoder encoder = AWTSequenceEncoder.createSequenceEncoder(file, fps);

        for (KeyFrame frame : frames) {
            painter.getConverter().setXShape(frame.xMin, frame.xMax);
            painter.getConverter().setYShape(frame.yMin, frame.yMax);

            BufferedImage img = painter.createImage();
            int w = img.getWidth();
            int h = img.getHeight();

            if (w % 2 != 0) w--;
            if (h % 2 != 0) h--;

            BufferedImage evenImg = img.getSubimage(0, 0, w, h);
            encoder.encodeImage(evenImg);
        }

        encoder.finish();
    }
}