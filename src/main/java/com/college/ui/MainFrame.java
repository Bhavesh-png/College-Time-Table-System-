package com.college.ui;

import com.college.ui.components.Sidebar;
import com.college.util.AnimationUtils;
import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class MainFrame extends JFrame {
    private Sidebar sidebar;
    private JPanel contentArea;
    private CardLayout cardLayout;

    public MainFrame() {
        initUI();
    }

    private void initUI() {
        setTitle("College Timetable Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1000, 700));

        // Set Application Icon
        try {
            File logoFile = new File("ses_logo.png");
            if (logoFile.exists()) {
                setIconImage(new ImageIcon(logoFile.getAbsolutePath()).getImage());
            }
        } catch (Exception e) {}

        JPanel root = new JPanel(new BorderLayout());
        setContentPane(root);

        sidebar = new Sidebar(this);
        root.add(sidebar, BorderLayout.WEST);

        cardLayout = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setBackground(UIManager.getColor("Panel.background"));
        contentArea.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        initCards();

        root.add(contentArea, BorderLayout.CENTER);

        setVisible(true);
    }

    private void initCards() {
        contentArea.add(new DashboardPanel(this), "Dashboard");
        contentArea.add(new TimetablePanel(), "Timetable");
        contentArea.add(new AddSchedulePanel(), "Add Schedule");
        contentArea.add(new SettingsPanel(), "Settings");
    }

    public void showCard(String name) {
        cardLayout.show(contentArea, name);
        for (Component comp : contentArea.getComponents()) {
            if (comp.isVisible()) {
                if (comp instanceof Refreshable) {
                    ((Refreshable) comp).refreshData();
                }
                if (comp instanceof AnimationUtils.FadePanel) {
                    AnimationUtils.fadeIn((AnimationUtils.FadePanel) comp);
                }
            }
        }
    }
}
