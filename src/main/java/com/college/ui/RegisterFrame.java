package com.college.ui;

import com.college.service.AuthService;
import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class RegisterFrame extends JFrame {
    private JTextField nameField, userField, secretField;
    private JPasswordField passField;
    private Point mouseDownCompCoords = null;

    public RegisterFrame() {
        initUI();
    }

    private void initUI() {
        setTitle("SES CMS - Create Account");
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setSize(540, 820);
        setLocationRelativeTo(null);

        // Set Application Icon
        try {
            File logoFile = new File("ses_logo.png");
            if (logoFile.exists()) {
                setIconImage(new ImageIcon(logoFile.getAbsolutePath()).getImage());
            }
        } catch (Exception e) {}

        JPanel root = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(41, 121, 255), 0, getHeight(), new Color(13, 71, 161));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 50, 50);
                g2.dispose();
            }
        };
        root.setOpaque(false);
        setContentPane(root);

        // Dragging Support
        root.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { mouseDownCompCoords = e.getPoint(); }
        });
        root.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point currCoords = e.getLocationOnScreen();
                setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y);
            }
        });

        // Close Button
        JButton closeBtn = new JButton("\u2715");
        closeBtn.setFont(new Font("Inter", Font.BOLD, 18));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setBorderPainted(false);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> System.exit(0));
        
        JPanel closeWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        closeWrapper.setOpaque(false);
        closeWrapper.add(closeBtn);

        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(440, 700));
        card.setBackground(Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(40, 45, 40, 45));
        card.putClientProperty(FlatClientProperties.STYLE, "arc: 60; background: #FFFFFF;");

        // Centered Header
        JPanel logoSection = new JPanel(new GridBagLayout());
        logoSection.setOpaque(false);
        
        GridBagConstraints gbcHeader = new GridBagConstraints();
        gbcHeader.gridx = 0; gbcHeader.gridy = 0; gbcHeader.anchor = GridBagConstraints.CENTER;

        try {
            File f = new File("ses_logo.png");
            if (f.exists()) {
                Image img = new ImageIcon(f.getAbsolutePath()).getImage().getScaledInstance(-1, 110, Image.SCALE_SMOOTH);
                logoSection.add(new JLabel(new ImageIcon(img)), gbcHeader);
            }
        } catch (Exception e) {}

        gbcHeader.gridy = 1;
        gbcHeader.insets = new Insets(15, 0, 0, 0);
        JLabel title = new JLabel("Join SES CMS");
        title.setFont(new Font("Inter", Font.BOLD, 26));
        logoSection.add(title, gbcHeader);
        
        card.add(logoSection);
        card.add(Box.createRigidArea(new Dimension(0, 40)));

        nameField = createField("Full Name");
        userField = createField("Username");
        passField = new JPasswordField();
        passField.setFont(new Font("Inter", Font.PLAIN, 15));
        passField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Password");
        passField.putClientProperty(FlatClientProperties.STYLE, "arc: 15;");
        passField.setMaximumSize(new Dimension(340, 50));
        passField.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        secretField = createField("Secret Answer (Needed for recovery)");

        card.add(nameField);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(userField);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(passField);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(secretField);
        card.add(Box.createRigidArea(new Dimension(0, 40)));

        JButton regBtn = new JButton("Create Account");
        regBtn.setMaximumSize(new Dimension(340, 55));
        regBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        regBtn.putClientProperty(FlatClientProperties.STYLE, "arc: 999; background: #1976D2; foreground: #FFFFFF;");
        regBtn.setFont(new Font("Inter", Font.BOLD, 16));
        regBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        regBtn.addActionListener(e -> handleRegister());

        card.add(regBtn);
        card.add(Box.createRigidArea(new Dimension(0, 25)));

        JButton backBtn = new JButton("Already have an account? Sign In");
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setForeground(new Color(41, 121, 255));
        backBtn.setFont(new Font("Inter", Font.BOLD, 14));
        backBtn.addActionListener(e -> { dispose(); new LoginFrame().setVisible(true); });
        card.add(backBtn);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.insets = new Insets(10, 10, 0, 10);
        root.add(closeWrapper, gbc);
        
        gbc.gridy = 1; gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 40, 40, 40);
        root.add(card, gbc);
    }

    private JTextField createField(String p) {
        JTextField f = new JTextField();
        f.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, p);
        f.putClientProperty(FlatClientProperties.STYLE, "arc: 15;");
        f.setMaximumSize(new Dimension(340, 50));
        f.setAlignmentX(Component.CENTER_ALIGNMENT);
        f.setFont(new Font("Inter", Font.PLAIN, 15));
        return f;
    }

    private void handleRegister() {
        if (AuthService.register(userField.getText(), new String(passField.getPassword()), secretField.getText())) {
            JOptionPane.showMessageDialog(this, "Account Created Successfully!");
            dispose();
            new LoginFrame().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Username already exists or Invalid info!");
        }
    }
}
