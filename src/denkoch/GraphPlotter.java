package denkoch;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * The GraphPlotter class provides a graphical representation of execution times for different requests
 * using a specified scheduler.
 */
public class GraphPlotter {

    /**
     * Plots the execution times on a graph.
     *
     * @param times a list of execution times in milliseconds.
     * @param schedulerName the name of the scheduler used.
     */
    public static void plot(List<Double> times, String schedulerName) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Graph - " + schedulerName);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.add(new GraphPanel(times, schedulerName));
            frame.setVisible(true);
        });
    }

    static class GraphPanel extends JPanel {
        private final List<Double> times;
        private final String schedulerName;

        public GraphPanel(List<Double> times, String schedulerName) {
            this.times = times;
            this.schedulerName = schedulerName;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            int width = getWidth();
            int height = getHeight();
            int padding = 50;
            int labelPadding = 30;
            int pointWidth = 8;
            int numYTicks = 10;

            double xScale = (double) (width - 2 * padding - labelPadding) / (times.size() - 1);
            double yScale = (double) (height - 2 * padding - labelPadding) / (getMaxTime() - getMinTime());

            g2.setColor(Color.WHITE);
            g2.fillRect(padding + labelPadding, padding, width - 2 * padding - labelPadding, height - 2 * padding - labelPadding);

            g2.setColor(Color.LIGHT_GRAY);
            for (int i = 0; i <= numYTicks; i++) {
                int y = (int) (height - padding - (i * (height - 2 * padding) / numYTicks));
                g2.drawLine(padding + labelPadding, y, width - padding, y);
            }
            for (int i = 0; i < times.size(); i++) {
                int x = (int) (i * xScale + padding + labelPadding);
                g2.drawLine(x, height - padding, x, padding);
            }

            g2.setColor(Color.BLACK);
            g2.drawLine(padding + labelPadding, height - padding, padding + labelPadding, padding); // Y-axis
            g2.drawLine(padding + labelPadding, height - padding, width - padding, height - padding); // X-axis

            for (int i = 0; i <= numYTicks; i++) {
                int y = (int) (height - padding - (i * (height - 2 * padding) / numYTicks));
                g2.drawLine(padding + labelPadding - 5, y, padding + labelPadding, y);
                String label = String.format("%.1f", getMinTime() + i * (getMaxTime() - getMinTime()) / numYTicks);
                g2.drawString(label, padding, y + 5);
            }

            for (int i = 0; i < times.size(); i++) {
                int x = (int) (i * xScale + padding + labelPadding);
                g2.drawLine(x, height - padding, x, height - padding + 5);
                g2.drawString(String.valueOf(i + 1), x - 5, height - padding + 20);
            }

            g2.setColor(Color.BLUE);
            for (int i = 0; i < times.size(); i++) {
                int x = (int) (i * xScale + padding + labelPadding);
                int y = (int) ((getMaxTime() - times.get(i)) * yScale + padding);
                g2.fillOval(x - pointWidth / 2, y - pointWidth / 2, pointWidth, pointWidth);

                if (i > 0) {
                    int prevX = (int) ((i - 1) * xScale + padding + labelPadding);
                    int prevY = (int) ((getMaxTime() - times.get(i - 1)) * yScale + padding);

                    g2.drawLine(prevX, prevY, x, y);
                }
            }

            g2.setColor(Color.BLACK);
            g2.drawString("Execution Time (ms)", padding, padding - 10);
            g2.drawString("Request", width - padding - labelPadding, height - padding + 40);
            g2.drawString("Scheduler: " + schedulerName, padding, height - padding + 40);
        }

        private double getMaxTime() {
            return times.stream().max(Double::compare).orElse(1.0);
        }

        private double getMinTime() {
            return times.stream().min(Double::compare).orElse(0.0);
        }
    }
}

