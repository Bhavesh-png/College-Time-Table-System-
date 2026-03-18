package com.college.ui.components;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CalendarWidget extends JComponent {
    public CalendarWidget() {
        setPreferredSize(new Dimension(80, 100));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // Calendar Base
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(5, 5, w - 10, h - 10, 15, 15);
        
        // Ribbon (Top part)
        g2.setColor(new Color(255, 82, 82));
        g2.fillRoundRect(5, 5, w - 10, 30, 15, 15);
        g2.fillRect(5, 20, w - 10, 15); // Flatten bottom of ribbon

        SimpleDateFormat monthFmt = new SimpleDateFormat("MMM");
        SimpleDateFormat dayFmt = new SimpleDateFormat("dd");
        SimpleDateFormat yearFmt = new SimpleDateFormat("yyyy");

        String month = monthFmt.format(new Date()).toUpperCase();
        String day = dayFmt.format(new Date());
        String year = yearFmt.format(new Date());

        // Month Text
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Inter", Font.BOLD, 14));
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(month, (w - fm.stringWidth(month)) / 2, 25);

        // Day Text
        g2.setColor(new Color(33, 33, 33));
        g2.setFont(new Font("Inter", Font.BOLD, 32));
        fm = g2.getFontMetrics();
        g2.drawString(day, (w - fm.stringWidth(day)) / 2, 68);

        // Year Text
        g2.setColor(Color.GRAY);
        g2.setFont(new Font("Inter", Font.BOLD, 12));
        fm = g2.getFontMetrics();
        g2.drawString(year, (w - fm.stringWidth(year)) / 2, 88);

        g2.dispose();
    }
}
