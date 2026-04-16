package ru.gr0946x.ui.animation;

import ru.gr0946x.Converter;
import ru.gr0946x.ui.SelectablePanel;
import ru.gr0946x.ui.painting.FractalPainter;
import ru.gr0946x.ui.fractals.Mandelbrot;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TourWindow extends JFrame {

    private final List<KeyFrame> frames = new ArrayList<>();
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> frameList = new JList<>(listModel);

    public TourWindow(FractalPainter originalPainter) {

        setTitle("Экскурсия по фракталу");
        setSize(1000, 700);

        Converter conv = new Converter(-2.0, 1.0, -1.0, 1.0);

        FractalPainter painter = new FractalPainter(
                new Mandelbrot(),
                conv,
                (value) -> {
                    if (value == 1.0) return Color.BLACK;
                    float r = (float)Math.abs(Math.sin(5 * value));
                    float g = (float)Math.abs(Math.cos(8 * value) * Math.sin(3 * value));
                    float b = (float)Math.abs((Math.sin(7 * value) + Math.cos(15 * value)) / 2f);
                    return new Color(r, g, b);
                }
        );

        SelectablePanel fractalPanel = new SelectablePanel(painter, conv);
        fractalPanel.addSelectListener(rect -> {
            double xMin = conv.xScr2Crt(rect.x);
            double xMax = conv.xScr2Crt(rect.x + rect.width);
            double yMin = conv.yScr2Crt(rect.y + rect.height);
            double yMax = conv.yScr2Crt(rect.y);

            fractalPanel.applyZoom(xMin, xMax, yMin, yMax);
            fractalPanel.repaint();
        });

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());

        JPanel controls = new JPanel();
        controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));

        JButton addBtn = new JButton("Добавить кадр");
        JButton removeBtn = new JButton("Удалить кадр");
        JButton renderBtn = new JButton("Создать видео");

        JSpinner durationSpinner = new JSpinner(
                new SpinnerNumberModel(2.0, 0.1, 60.0, 0.1)
        );

        controls.add(new JLabel("Длительность (сек):"));
        controls.add(durationSpinner);
        controls.add(Box.createVerticalStrut(10));
        controls.add(addBtn);
        controls.add(removeBtn);
        controls.add(renderBtn);

        JScrollPane listScroll = new JScrollPane(frameList);

        rightPanel.add(controls, BorderLayout.NORTH);
        rightPanel.add(listScroll, BorderLayout.CENTER);

        addBtn.addActionListener(e -> {
            double duration = (double) durationSpinner.getValue();

            KeyFrame kf = new KeyFrame(
                    conv.getXMin(), conv.getXMax(),
                    conv.getYMin(), conv.getYMax(),
                    duration
            );

            frames.add(kf);
            listModel.addElement("Кадр " + frames.size());
        });

        removeBtn.addActionListener(e -> {
            int i = frameList.getSelectedIndex();
            if (i >= 0) {
                frames.remove(i);
                listModel.remove(i);
            }
        });

        frameList.addListSelectionListener(e -> {
            int i = frameList.getSelectedIndex();
            if (i >= 0) {
                KeyFrame k = frames.get(i);
                fractalPanel.applyZoom(k.xMin, k.xMax, k.yMin, k.yMax);
            }
        });

        renderBtn.addActionListener(e -> {
            try {
                var generated = AnimationGenerator.generate(frames, 30);

                JFileChooser chooser = new JFileChooser();
                chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                        "MP4 Video (*.mp4)", "mp4"
                ));

                if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {

                    File file = chooser.getSelectedFile();

                    if (!file.getName().toLowerCase().endsWith(".mp4")) {
                        file = new File(file.getAbsolutePath() + ".mp4");
                    }

                    if (file.exists()) {
                        int res = JOptionPane.showConfirmDialog(
                                this,
                                "Файл уже существует. Перезаписать?",
                                "Подтверждение",
                                JOptionPane.YES_NO_OPTION
                        );
                        if (res != JOptionPane.YES_OPTION) return;
                    }

                    VideoExporter.export(generated, 30, painter, file);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        setLayout(new BorderLayout());
        add(fractalPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
    }
}