package com.college.ui;

import com.college.model.TimetableEntry;
import com.college.service.TimetableService;
import com.college.ui.components.AnalogClock;
import com.college.ui.components.CalendarWidget;
import com.college.util.AnimationUtils.FadePanel;
import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class DashboardPanel extends FadePanel implements Refreshable {
    private MainFrame mainFrame;
    private JPanel statsPanel;
    private JLabel desc;
    private AnalogClock analogClock;
    private CalendarWidget calendarWidget;
    private Timer clockTimer;

    public DashboardPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initUI();
        startClock();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(20, 40, 30, 40));

        // Top Greeting Section
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        
        JLabel title = new JLabel("Dashboard Overview");
        title.setFont(new Font("Inter", Font.BOLD, 32));
        top.add(title, BorderLayout.NORTH);
        
        JLabel welcome = new JLabel("Welcome back, Administrator. Check your system updates below.");
        welcome.setFont(new Font("Inter", Font.PLAIN, 15));
        welcome.setForeground(Color.GRAY);
        welcome.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));
        top.add(welcome, BorderLayout.CENTER);
        
        add(top, BorderLayout.NORTH);

        // Stats Cards Section
        statsPanel = new JPanel(new GridLayout(1, 4, 30, 0));
        statsPanel.setOpaque(false);
        
        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.add(statsPanel, BorderLayout.NORTH);
        
        // Premium Banner
        JPanel welcomeCard = new JPanel(new BorderLayout());
        welcomeCard.putClientProperty(FlatClientProperties.STYLE, "arc: 45; background: #1565C0;");
        welcomeCard.setBorder(BorderFactory.createEmptyBorder(45, 45, 45, 45));
        
        JPanel leftContent = new JPanel();
        leftContent.setLayout(new BoxLayout(leftContent, BoxLayout.Y_AXIS));
        leftContent.setOpaque(false);
        
        JLabel bannerTitle = new JLabel("Smart Academic Scheduling");
        bannerTitle.setFont(new Font("Inter", Font.BOLD, 28));
        bannerTitle.setForeground(Color.WHITE);
        leftContent.add(bannerTitle);
        
        leftContent.add(Box.createRigidArea(new Dimension(0, 10)));
        
        desc = new JLabel("");
        desc.setFont(new Font("Inter", Font.PLAIN, 16));
        desc.setForeground(new Color(230, 230, 230));
        leftContent.add(desc);

        leftContent.add(Box.createRigidArea(new Dimension(0, 45)));
        
        JPanel quickActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        quickActions.setOpaque(false);
        quickActions.add(createQuickBtn("📅 View Timetable", "#0D47A1", "Timetable"));
        quickActions.add(createQuickBtn("➕ New Entry", "#FFFFFF22", "Add Schedule"));
        quickActions.add(createQuickBtn("⚙️ Settings", "#FFFFFF22", "Settings"));
        leftContent.add(quickActions);
        
        welcomeCard.add(leftContent, BorderLayout.CENTER);

        // Analog Clock & Calendar Section
        JPanel timeSection = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 0));
        timeSection.setOpaque(false);
        
        calendarWidget = new CalendarWidget();
        analogClock = new AnalogClock();
        
        timeSection.add(calendarWidget);
        timeSection.add(analogClock);
        
        welcomeCard.add(timeSection, BorderLayout.EAST);
        
        JPanel bannerWrapper = new JPanel(new BorderLayout());
        bannerWrapper.setOpaque(false);
        bannerWrapper.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));
        bannerWrapper.add(welcomeCard);
        
        center.add(bannerWrapper, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);
        
        refreshData();
    }

    private JButton createQuickBtn(String text, String bg, String cardName) {
        JButton b = new JButton(text);
        b.setFont(new Font("Inter", Font.BOLD, 14));
        b.setForeground(Color.WHITE);
        b.setPreferredSize(new Dimension(170, 50));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.putClientProperty(FlatClientProperties.STYLE, "arc: 999; background: " + bg + "; borderPainted: false;");
        b.addActionListener(e -> mainFrame.showCard(cardName));
        return b;
    }

    private void startClock() {
        clockTimer = new Timer(1000, e -> {
            analogClock.repaint();
            calendarWidget.repaint();
        });
        clockTimer.start();
    }

    @Override
    public void refreshData() {
        statsPanel.removeAll();
        List<TimetableEntry> entries = TimetableService.getAllEntries();
        int totalClasses = entries.size();
        Set<String> faculties = new HashSet<>();
        for (TimetableEntry e : entries) faculties.add(e.getFaculty());
        int facultyCount = faculties.size();
        int occupancy = Math.min(100, Math.max(0, totalClasses * 8));
        int activeHours = Math.min(12, totalClasses);

        statsPanel.add(createStatCard("Active Classes", String.valueOf(totalClasses), new Color(33, 150, 243)));
        statsPanel.add(createStatCard("Faculty Members", String.valueOf(facultyCount), new Color(156, 39, 176)));
        statsPanel.add(createStatCard("Room Occupancy", occupancy + "%", new Color(0, 150, 136)));
        statsPanel.add(createStatCard("Work Hours", activeHours + "h", new Color(255, 87, 34)));

        desc.setText("System currently tracking " + totalClasses + " sessions for " + facultyCount + " departments.");
        statsPanel.revalidate();
        statsPanel.repaint();
    }

    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(UIManager.getColor("Panel.background"));
        card.putClientProperty(FlatClientProperties.STYLE, "arc: 25;");
        card.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        JLabel t = new JLabel(title.toUpperCase());
        t.setFont(new Font("Inter", Font.BOLD, 12));
        t.setForeground(new Color(140, 140, 140));
        JLabel v = new JLabel(value);
        v.setFont(new Font("Inter", Font.BOLD, 42));
        v.setForeground(color);
        card.add(t, BorderLayout.NORTH);
        card.add(v, BorderLayout.CENTER);
        return card;
    }
}
