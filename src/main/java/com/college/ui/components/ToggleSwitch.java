package com.college.ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class ToggleSwitch extends JComponent {
    private boolean selected = false;
    private float location = 2;
    private final Timer timer;
    private final List<ActionListener> listeners = new ArrayList<>();

    public ToggleSwitch() {
        setPreferredSize(new Dimension(60, 30));
        setMinimumSize(new Dimension(60, 30));
        setMaximumSize(new Dimension(60, 30));
        
        timer = new Timer(10, e -> {
            if (selected) {
                if (location < 32) location += 3.0f;
                else ((Timer)e.getSource()).stop();
            } else {
                if (location > 2) location -= 3.0f;
                else ((Timer)e.getSource()).stop();
            }
            repaint();
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                setSelected(!selected);
                fireAction();
            }
        });
    }

    public boolean isSelected() { return selected; }
    
    public void setSelected(boolean selected) {
        this.selected = selected;
        timer.start();
        repaint();
    }

    public void addActionListener(ActionListener l) {
        listeners.add(l);
    }

    private void fireAction() {
        ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "toggle");
        for (ActionListener l : listeners) l.actionPerformed(e);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int w = getWidth();
        int h = getHeight();
        int margin = 3;
        
        // Track
        g2.setColor(selected ? new Color(33, 150, 243) : new Color(210, 210, 210));
        g2.fillRoundRect(0, 0, w, h, h, h);
        
        // Knob
        g2.setColor(Color.WHITE);
        int knobSize = h - (margin * 2);
        g2.fillOval((int)location, margin, knobSize, knobSize);
        
        g2.dispose();
    }
}
