package com.college.ui.components;

import com.college.ui.MainFrame;
import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class Sidebar extends JPanel {
    private MainFrame mainFrame;
    private JPanel buttonsPanel;

    public Sidebar(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initUI();
    }

    private void initUI() {
        setPreferredSize(new Dimension(340, 0));
        setBackground(new Color(21, 101, 192));
        setLayout(new BorderLayout());

        // Header Section (Logo + Name)
        JPanel header = new JPanel();
        header.setLayout(new GridBagLayout());
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(40, 0, 30, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        try {
            File logoFile = new File("ses_logo.png");
            if (logoFile.exists()) {
                ImageIcon icon = new ImageIcon(logoFile.getAbsolutePath());
                Image img = icon.getImage().getScaledInstance(-1, 120, Image.SCALE_SMOOTH);
                JLabel logoLabel = new JLabel(new ImageIcon(img));
                header.add(logoLabel, gbc);
            }
        } catch (Exception e) {}

        gbc.gridy = 1;
        gbc.insets = new Insets(15, 0, 0, 0);
        JLabel instName = new JLabel("<html><center>R. C. Patel College of Engineering<br>& Polytechnic, Shirpur</center></html>");
        instName.setFont(new Font("Inter", Font.BOLD, 14));
        instName.setForeground(Color.WHITE);
        header.add(instName, gbc);

        add(header, BorderLayout.NORTH);

        // Sidebar Buttons Panel
        buttonsPanel = new JPanel();
        buttonsPanel.setOpaque(false);
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 0, 25));

        addNavButton("Dashboard", "Dashboard");
        addNavButton("Timetable", "Timetable");
        addNavButton("Add Schedule", "Add Schedule");
        addNavButton("Settings", "Settings");

        add(buttonsPanel, BorderLayout.CENTER);

        // Bottom profile section
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        userPanel.setOpaque(false);
        JLabel avatar = new JLabel("\uD83D\uDC64");
        avatar.setFont(new Font("Inter", Font.PLAIN, 32));
        avatar.setForeground(Color.WHITE);
        
        JPanel info = new JPanel(new GridLayout(2, 1));
        info.setOpaque(false);
        JLabel name = new JLabel("Admin User");
        name.setFont(new Font("Inter", Font.BOLD, 14));
        name.setForeground(Color.WHITE);
        JLabel role = new JLabel("Administrator");
        role.setFont(new Font("Inter", Font.PLAIN, 12));
        role.setForeground(new Color(180, 210, 255));
        info.add(name);
        info.add(role);
        
        userPanel.add(avatar);
        userPanel.add(info);
        bottomPanel.add(userPanel, BorderLayout.NORTH);

        JButton logout = new JButton("Logout");
        // Added border to logout button as well
        logout.putClientProperty(FlatClientProperties.STYLE, "arc: 999; background: #0D47A1; foreground: #FFFFFF; borderWidth: 2; borderColor: #1565C0;");
        logout.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        logout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logout.addActionListener(e -> System.exit(0));
        
        JPanel logOutWrapper = new JPanel(new BorderLayout());
        logOutWrapper.setOpaque(false);
        logOutWrapper.add(logout);
        logOutWrapper.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        bottomPanel.add(logOutWrapper, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void addNavButton(String text, String cardName) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btn.setPreferredSize(new Dimension(Integer.MAX_VALUE, 50));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Added BORDER with contrast - borderWidth: 2; borderColor: #64B5F6;
        btn.putClientProperty(FlatClientProperties.STYLE, "arc: 999; background: #1976D2; foreground: #FFFFFF; borderWidth: 2; borderColor: #64B5F6;");
        btn.setFont(new Font("Inter", Font.BOLD, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);
        
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { 
                btn.setBackground(new Color(13, 71, 161)); 
                btn.putClientProperty(FlatClientProperties.STYLE, "arc: 999; background: #0D47A1; foreground: #FFFFFF; borderWidth: 2; borderColor: #FFFFFF;");
            }
            @Override
            public void mouseExited(MouseEvent e) { 
                btn.setBackground(new Color(25, 118, 210)); 
                btn.putClientProperty(FlatClientProperties.STYLE, "arc: 999; background: #1976D2; foreground: #FFFFFF; borderWidth: 2; borderColor: #64B5F6;");
            }
        });
        btn.addActionListener(e -> mainFrame.showCard(cardName));
        buttonsPanel.add(btn);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
    }
}
