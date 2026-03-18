package com.college.ui.components;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;

public class AnalogClock extends JComponent {
    public AnalogClock() {
        setPreferredSize(new Dimension(140, 140));
        setMinimumSize(new Dimension(140, 140));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int size = Math.min(w, h) - 10;
        int x = (w - size) / 2;
        int y = (h - size) / 2;

        // Glass Frame
        g2.setColor(new Color(255, 255, 255, 30));
        g2.fillOval(x, y, size, size);
        g2.setStroke(new BasicStroke(2f));
        g2.setColor(new Color(255, 255, 255, 120));
        g2.drawOval(x, y, size, size);

        // Center point
        int cx = w / 2;
        int cy = h / 2;

        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR);
        int min = cal.get(Calendar.MINUTE);
        int sec = cal.get(Calendar.SECOND);

        // Hour Hand
        double hAngle = Math.toRadians((hour * 30) + (min * 0.5) - 90);
        drawHand(g2, cx, cy, hAngle, size * 0.25, new BasicStroke(4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND), Color.WHITE);

        // Minute Hand
        double mAngle = Math.toRadians((min * 6) - 90);
        drawHand(g2, cx, cy, mAngle, size * 0.35, new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND), new Color(220, 220, 220));

        // Second Hand
        double sAngle = Math.toRadians((sec * 6) - 90);
        drawHand(g2, cx, cy, sAngle, size * 0.42, new BasicStroke(1.5f), new Color(255, 82, 82));

        // Center Pin
        g2.setColor(Color.WHITE);
        g2.fillOval(cx - 4, cy - 4, 8, 8);

        g2.dispose();
    }

    private void drawHand(Graphics2D g2, int cx, int cy, double angle, double len, Stroke stroke, Color color) {
        g2.setStroke(stroke);
        g2.setColor(color);
        int x2 = (int) (cx + Math.cos(angle) * len);
        int y2 = (int) (cy + Math.sin(angle) * len);
        g2.drawLine(cx, cy, x2, y2);
    }
}
