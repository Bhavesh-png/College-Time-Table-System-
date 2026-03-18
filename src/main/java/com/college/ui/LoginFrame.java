package com.college.ui;

import com.college.service.AuthService;
import com.college.ui.components.RoundedTextField;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class LoginFrame extends JFrame {
    private RoundedTextField userField;
    private JPasswordField passField;
    private Point mouseDownCompCoords = null;

    public LoginFrame() {
        initUI();
    }

    private void initUI() {
        setTitle("College System - Login");
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setSize(540, 820); // Slightly larger for bigger fonts
        setLocationRelativeTo(null);

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
                GradientPaint gp = new GradientPaint(0, 0, new Color(30, 136, 229), 0, getHeight(), new Color(13, 71, 161));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 50, 50);
                g2.dispose();
            }
        };
        root.setOpaque(false);
        setContentPane(root);

        // Dragging
        root.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { mouseDownCompCoords = e.getPoint(); }
            public void mouseReleased(MouseEvent e) { mouseDownCompCoords = null; }
        });
        root.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point currCoords = e.getLocationOnScreen();
                setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y);
            }
        });

        JButton closeBtn = new JButton("\u2715");
        closeBtn.setFont(new Font("Inter", Font.BOLD, 20));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setBorderPainted(false);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> System.exit(0));
        
        JPanel closeWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        closeWrapper.setOpaque(false);
        closeWrapper.add(closeBtn);
        
        // Login Card with LARGER CURVE (Arc 60)
        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(440, 680));
        card.setBackground(Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(45, 45, 45, 45));
        card.putClientProperty(FlatClientProperties.STYLE, "arc: 60; background: #FFFFFF;");

        // Header
        JPanel logoSection = new JPanel(new GridBagLayout());
        logoSection.setOpaque(false);
        logoSection.setAlignmentX(Component.CENTER_ALIGNMENT);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.CENTER;

        try {
            File f = new File("ses_logo.png");
            if (f.exists()) {
                Image img = new ImageIcon(f.getAbsolutePath()).getImage().getScaledInstance(-1, 140, Image.SCALE_SMOOTH);
                logoSection.add(new JLabel(new ImageIcon(img)), gbc);
            }
        } catch (Exception e) {}

        gbc.gridy = 1;
        gbc.insets = new Insets(20, 0, 0, 0);
        // INCREASED LABEL SIZE
        JLabel instName = new JLabel("<html><center><font color='#333333'>R. C. Patel College of Engineering<br>& Polytechnic, Shirpur</font></center></html>");
        instName.setFont(new Font("Inter", Font.BOLD, 19)); 
        logoSection.add(instName, gbc);

        card.add(logoSection);
        card.add(Box.createRigidArea(new Dimension(0, 45)));

        // Form (INCREASED INPUT SIZES)
        userField = new RoundedTextField("Username");
        userField.setFont(new Font("Inter", Font.PLAIN, 16));
        
        passField = new JPasswordField();
        passField.setFont(new Font("Inter", Font.PLAIN, 16));
        passField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Password");
        passField.putClientProperty(FlatClientProperties.STYLE, "arc: 18; focusWidth: 2;");
        
        userField.setMaximumSize(new Dimension(340, 55));
        passField.setMaximumSize(new Dimension(340, 55));
        userField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passField.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(userField);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(passField);
        
        JButton forgot = new JButton("Forgot Password?");
        forgot.setAlignmentX(Component.CENTER_ALIGNMENT);
        forgot.setBorderPainted(false);
        forgot.setContentAreaFilled(false);
        forgot.setForeground(Color.GRAY);
        forgot.setFont(new Font("Inter", Font.PLAIN, 13));
        forgot.addActionListener(e -> new PasswordResetFrame().setVisible(true));
        card.add(forgot);
        
        card.add(Box.createRigidArea(new Dimension(0, 35)));

        // INCREASED BUTTON SIZE
        JButton loginBtn = new JButton("Sign In");
        loginBtn.setMaximumSize(new Dimension(340, 60));
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.putClientProperty(FlatClientProperties.STYLE, "arc: 999; background: #1976D2; foreground: #FFFFFF;");
        loginBtn.setFont(new Font("Inter", Font.BOLD, 17));
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginBtn.addActionListener(e -> handleLogin());

        card.add(loginBtn);
        card.add(Box.createRigidArea(new Dimension(0, 40)));
        
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(340, 1));
        card.add(sep);
        card.add(Box.createRigidArea(new Dimension(0, 25)));
        
        JButton regBtn = new JButton("Don't have an account? Sign Up");
        regBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        regBtn.setBorderPainted(false);
        regBtn.setContentAreaFilled(false);
        regBtn.setForeground(new Color(30, 136, 229));
        regBtn.setFont(new Font("Inter", Font.BOLD, 15));
        regBtn.addActionListener(e -> { dispose(); new RegisterFrame().setVisible(true); });
        card.add(regBtn);

        gbc.gridy = 0; gbc.insets = new Insets(10, 10, 0, 10);
        gbc.anchor = GridBagConstraints.NORTHEAST;
        root.add(closeWrapper, gbc);
        
        gbc.gridy = 1; gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 50, 50, 50);
        root.add(card, gbc);

        setVisible(true);
    }

    private void handleLogin() {
        if (AuthService.login(userField.getText(), new String(passField.getPassword()))) {
            dispose();
            new MainFrame();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials!", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        FlatLightLaf.setup();
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
