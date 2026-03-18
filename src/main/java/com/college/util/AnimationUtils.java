package com.college.util;

import javax.swing.*;
import java.awt.*;

public class AnimationUtils {
    public static void fadeIn(FadePanel panel) {
        if (panel == null) return;
        panel.setAlpha(0.0f);
        Timer timer = new Timer(20, null);
        timer.addActionListener(e -> {
            float cur = panel.getAlpha();
            cur += 0.1f; // Faster fade for responsiveness
            if (cur >= 1.0f) {
                cur = 1.0f;
                timer.stop();
            }
            panel.setAlpha(cur);
            panel.repaint();
        });
        timer.start();
    }

    public static abstract class FadePanel extends JPanel {
        private float opacity = 1.0f;

        public void setAlpha(float opacity) {
            this.opacity = Math.max(0.0f, Math.min(1.0f, opacity));
        }
        
        public float getAlpha() {
            return opacity;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
            super.paintComponent(g2);
            g2.dispose();
        }
    }
}
