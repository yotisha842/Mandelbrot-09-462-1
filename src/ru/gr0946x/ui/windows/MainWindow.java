package ru.gr0946x.ui.windows;

import ru.gr0946x.Converter;
import ru.gr0946x.core.fractals.DynamicIterations;
import ru.gr0946x.core.fractals.Fractal;
import ru.gr0946x.core.fractals.FractalConfig;
import ru.gr0946x.core.fractals.Mandelbrot;
import ru.gr0946x.core.model.FractalSession;
import ru.gr0946x.core.rendering.ColorFunction;
import ru.gr0946x.core.rendering.FractalPainter;
import ru.gr0946x.core.rendering.Painter;
import ru.gr0946x.ui.menus.MenuManager;
import ru.gr0946x.ui.components.SelectablePanel;
import ru.smak.math.Complex;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static java.lang.Math.*;

/**
 * Главное окно приложения для просмотра множества Мандельброта.
 *
 * Функциональность:
 * - Отображение фрактала через FractalPainter и SelectablePanel
 * - Масштабирование выделением (ЛКМ), перемещение (ПКМ), отмена/повтор (Ctrl+Z/Y)
 * - Переключение формул фракталов и цветовых схем через меню
 * - Открытие окна множества Жюлиа по клику на точку
 * - Сохранение/загрузка состояния через .frac-файлы
 * - Адаптивное число итераций при зуме (DynamicIterations)
 *
 * Является точкой входа для пользовательского взаимодействия с приложением.
 */
public class MainWindow extends JFrame {

    private final SelectablePanel mainPanel;
    private Painter painter;
    private final Fractal mandelbrot;
    private final Converter conv;
    private final MenuManager menuManager;
    private final DynamicIterations dynamicIter;
    private JuliaWindow juliaWindow = null;

    private Point mousePressPoint = null;

    private int currentFractalIdx = 0;
    private int currentColorIdx = 0;

    public MainWindow() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800, 650));
        setTitle("Фрактал Множество Мандельброта");

        dynamicIter = new DynamicIterations();

        Mandelbrot mandelbrotImpl = new Mandelbrot();
        mandelbrotImpl.setDynamicIterations(dynamicIter);
        mandelbrot = mandelbrotImpl;

        conv = new Converter(-2.0, 1.0, -1.0, 1.0);
        painter = new FractalPainter(mandelbrot, conv, (value) -> {
            if (value == 1.0) return Color.BLACK;
            var r = (float) abs(sin(5 * value));
            var g = (float) abs(cos(8 * value) * sin(3 * value));
            var b = (float) abs((sin(7 * value) + cos(15 * value)) / 2f);
            return new Color(r, g, b);
        });

        mainPanel = new SelectablePanel(painter, conv);
        mainPanel.setBackground(Color.WHITE);

        mainPanel.setDynamicIterations(dynamicIter);

        mainPanel.addSelectListener((r) -> {
            if (r.width < 10 || r.height < 10) {
                return;
            }

            var xMin = conv.xScr2Crt(r.x);
            var xMax = conv.xScr2Crt(r.x + r.width);
            var yMin = conv.yScr2Crt(r.y + r.height);
            var yMax = conv.yScr2Crt(r.y);
            mainPanel.applyZoom(xMin, xMax, yMin, yMax);
        });

        mainPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    mousePressPoint = e.getPoint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && mousePressPoint != null) {
                    double dist = e.getPoint().distance(mousePressPoint);
                    if (dist < 5) {
                        double cX = conv.xScr2Crt(e.getX());
                        double cY = conv.yScr2Crt(e.getY());
                        Complex c = new Complex(cX, cY);

                        SwingUtilities.invokeLater(() -> {
                            if (juliaWindow != null && juliaWindow.isDisplayable()) {
                                juliaWindow.dispose();
                            }
                            juliaWindow = new JuliaWindow(c, "Множество Жюлиа", dynamicIter);
                            juliaWindow.setSize(800, 650);
                            juliaWindow.setLocationRelativeTo(MainWindow.this);
                            juliaWindow.setVisible(true);
                        });
                    }
                    mousePressPoint = null;
                }
            }
        });

        menuManager = new MenuManager((FractalPainter) painter, mainPanel, this);
        setJMenuBar(menuManager.createMenuBar());

        JMenu viewMenu = null;
        for (int i = 0; i < getJMenuBar().getMenuCount(); i++) {
            JMenu menu = getJMenuBar().getMenu(i);
            if (menu.getText().equals("Вид")) {
                viewMenu = menu;
                break;
            }
        }

        if (viewMenu != null) {
            viewMenu.addSeparator();
            JCheckBoxMenuItem dynamicIterItem = new JCheckBoxMenuItem("Динамическое число итераций");
            dynamicIterItem.addActionListener(e -> {
                boolean enabled = dynamicIterItem.isSelected();
                dynamicIter.setEnabled(enabled);
                mainPanel.repaint();
            });
            viewMenu.add(dynamicIterItem);
        }

        setContent();

        SwingUtilities.invokeLater(() -> mainPanel.repaint());
    }

    // Применяет новые настройки фрактала: формулу и цветовую схему.
    public void applySettings(int fIdx, int cIdx) {
        this.currentFractalIdx = fIdx;
        this.currentColorIdx = cIdx;

        FractalConfig.setDynamicIterations(dynamicIter);

        Fractal f = FractalConfig.FRACTALS.get(fIdx);
        ColorFunction c = FractalConfig.COLORS.get(cIdx);

        if (f instanceof Mandelbrot) {
            ((Mandelbrot) f).setDynamicIterations(dynamicIter);
        }

        painter = new FractalPainter(f, conv, c);

        if (mainPanel != null) {
            mainPanel.setPainter(painter);
        }
    }

    public int getCurrentFractalIdx() { return currentFractalIdx; }
    public int getCurrentColorIdx() { return currentColorIdx; }

    private void setContent() {
        var gl = new GroupLayout(getContentPane());
        setLayout(gl);
        gl.setVerticalGroup(gl.createSequentialGroup()
                .addGap(8)
                .addComponent(mainPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                .addGap(8)
        );
        gl.setHorizontalGroup(gl.createSequentialGroup()
                .addGap(8)
                .addComponent(mainPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                .addGap(8)
        );
    }

    public void restoreFromSession(FractalSession session) {
        if (session == null) return;

        conv.setXShape(session.xMin, session.xMax);
        conv.setYShape(session.yMin, session.yMax);

        applySettings(session.fractalIdx, session.colorIdx);

        if (mainPanel instanceof SelectablePanel) {
            try {
                java.lang.reflect.Field diField = SelectablePanel.class.getDeclaredField("dynamicIterations");
                diField.setAccessible(true);
                DynamicIterations di =
                        (DynamicIterations) diField.get(mainPanel);
                if (di != null) {
                    di.setEnabled(session.dynamicIterationsEnabled);
                    di.setLastWidth(session.dynamicIterationsLastWidth);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mainPanel.repaint();
    }
}