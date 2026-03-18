package com.college.ui;

import com.college.ui.components.TimetableGrid;
import com.college.util.AnimationUtils.FadePanel;
import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import java.awt.*;

public class TimetablePanel extends FadePanel implements Refreshable {
    private TimetableGrid grid;

    public TimetablePanel() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel title = new JLabel("Weekly Timetable");
        title.setFont(new Font("Inter", Font.BOLD, 28));
        add(title, BorderLayout.NORTH);

        JPanel gridWrapper = new JPanel(new BorderLayout());
        // Dynamic background
        gridWrapper.setBackground(UIManager.getColor("Panel.background"));
        gridWrapper.putClientProperty(FlatClientProperties.STYLE, "arc: 20;");
        gridWrapper.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        grid = new TimetableGrid();
        gridWrapper.add(grid, BorderLayout.CENTER);

        add(gridWrapper, BorderLayout.CENTER);
    }

    @Override
    public void refreshData() {
        if (grid != null) {
            grid.repaint();
        }
    }
}
