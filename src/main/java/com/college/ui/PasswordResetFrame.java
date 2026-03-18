package com.college.ui;

import com.college.service.AuthService;
import com.college.ui.components.RoundedTextField;
import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PasswordResetFrame extends JFrame {
    private RoundedTextField userField, secretField;
    private JPasswordField newPassField;
    private Point mouseDownCompCoords = null;

    public PasswordResetFrame() {
        initUI();
    }

    private void initUI() {
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setSize(480, 600);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(13, 71, 161), 0, getHeight(), new Color(25, 118, 210));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                g2.dispose();
            }
        };
        root.setOpaque(false);
        setContentPane(root);

        // Dragging
        root.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { mouseDownCompCoords = e.getPoint(); }
        });
        root.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point currCoords = e.getLocationOnScreen();
                setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y);
            }
        });

        JButton closeBtn = new JButton("\u2715");
        closeBtn.setFont(new Font("Inter", Font.BOLD, 18));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setBorderPainted(false);
        closeBtn.setContentAreaFilled(false);
        closeBtn.addActionListener(e -> dispose());
        
        JPanel closeWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        closeWrapper.setOpaque(false);
        closeWrapper.add(closeBtn);

        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(380, 500));
        card.setBackground(Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        card.putClientProperty(FlatClientProperties.STYLE, "arc: 40;");

        JLabel title = new JLabel("Password Recovery");
        title.setFont(new Font("Inter", Font.BOLD, 22));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(title);
        card.add(Box.createRigidArea(new Dimension(0, 30)));

        userField = new RoundedTextField("Username");
        secretField = new RoundedTextField("Your Secret Answer");
        newPassField = new JPasswordField();
        newPassField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "New Password");
        newPassField.putClientProperty(FlatClientProperties.STYLE, "arc: 12;");
        
        userField.setMaximumSize(new Dimension(300, 45));
        secretField.setMaximumSize(new Dimension(300, 45));
        newPassField.setMaximumSize(new Dimension(300, 45));
        userField.setAlignmentX(Component.CENTER_ALIGNMENT);
        secretField.setAlignmentX(Component.CENTER_ALIGNMENT);
        newPassField.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(userField); card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(secretField); card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(newPassField); card.add(Box.createRigidArea(new Dimension(0, 35)));

        JButton resetBtn = new JButton("Reset Password");
        resetBtn.setMaximumSize(new Dimension(300, 50));
        resetBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        resetBtn.putClientProperty(FlatClientProperties.STYLE, "arc: 999; background: #1976D2; foreground: #FFFFFF;");
        resetBtn.setFont(new Font("Inter", Font.BOLD, 15));
        resetBtn.addActionListener(e -> handleReset());
        card.add(resetBtn);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.NORTHEAST;
        root.add(closeWrapper, gbc);
        
        gbc.gridy = 1; gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 30, 30, 30);
        root.add(card, gbc);
        
        setVisible(true);
    }

    private void handleReset() {
        if (AuthService.resetPassword(userField.getText(), secretField.getText(), new String(newPassField.getPassword()))) {
            JOptionPane.showMessageDialog(this, "Password updated successfully!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid info! Secret answer is case-sensitive.", "Reset Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
