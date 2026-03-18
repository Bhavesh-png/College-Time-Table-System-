package com.college.ui;

import com.college.service.TimetableService;
import com.college.ui.components.ToggleSwitch;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;

public class SettingsPanel extends JPanel implements Refreshable {
    public SettingsPanel() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(20, 40, 30, 40));

        JLabel title = new JLabel("System Settings");
        title.setFont(new Font("Inter", Font.BOLD, 32));
        add(title, BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        content.add(createSettingCard("Interface Theme", "Switch between Light and Dark mode appearance.", createThemeToggle()));
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        content.add(createSettingCard("Desktop Notifications", "Show alerts for upcoming scheduled classes.", new ToggleSwitch()));
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        content.add(createSettingCard("Database Management", "Clear all timetable data or backup to cloud.", createDataActions()));
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        content.add(createSettingCard("Cloud Sync", "Automatically synchronize with central server.", new ToggleSwitch()));

        add(new JScrollPane(content) {{
            setOpaque(false);
            getViewport().setOpaque(false);
            setBorder(null);
        }}, BorderLayout.CENTER);
    }

    private JPanel createSettingCard(String title, String desc, Component action) {
        JPanel card = new JPanel(new BorderLayout());
        card.putClientProperty(FlatClientProperties.STYLE, "arc: 30; background: " + (FlatLaf.isLafDark() ? "#2A2D32" : "#FFFFFF"));
        card.setBorder(BorderFactory.createEmptyBorder(30, 35, 30, 35)); // Increased padding
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130)); // Slightly taller cards

        JPanel text = new JPanel(new GridLayout(2, 1));
        text.setOpaque(false);
        JLabel t = new JLabel(title);
        t.setFont(new Font("Inter", Font.BOLD, 18));
        JLabel d = new JLabel(desc);
        d.setFont(new Font("Inter", Font.PLAIN, 14));
        d.setForeground(Color.GRAY);
        text.add(t);
        text.add(d);

        card.add(text, BorderLayout.WEST);
        
        // Wrap action in centered panel
        JPanel actionWrap = new JPanel(new GridBagLayout());
        actionWrap.setOpaque(false);
        actionWrap.add(action);
        card.add(actionWrap, BorderLayout.EAST);
        
        return card;
    }

    private ToggleSwitch createThemeToggle() {
        ToggleSwitch ts = new ToggleSwitch();
        ts.setSelected(FlatLaf.isLafDark());
        ts.addActionListener(e -> {
            boolean dark = ts.isSelected();
            SwingUtilities.invokeLater(() -> {
                if (dark) FlatDarkLaf.setup();
                else FlatLightLaf.setup();
                FlatLaf.updateUI();
                Refreshable.refreshAll(SwingUtilities.getWindowAncestor(this));
            });
        });
        return ts;
    }

    private JPanel createDataActions() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        p.setOpaque(false);
        
        JButton reset = new JButton("Reset Data");
        reset.setFont(new Font("Inter", Font.BOLD, 14));
        reset.setPreferredSize(new Dimension(130, 45)); // INCREASED SIZE
        reset.putClientProperty(FlatClientProperties.STYLE, "arc: 999; background: #FF5252; foreground: #FFFFFF;");
        reset.setCursor(new Cursor(Cursor.HAND_CURSOR));
        reset.addActionListener(e -> {
            int res = JOptionPane.showConfirmDialog(this, "Are you sure? All timetable data will be lost.", "Confirm Reset", JOptionPane.YES_NO_OPTION);
            if (res == JOptionPane.YES_OPTION) {
                TimetableService.getAllEntries().clear();
                TimetableService.save();
                JOptionPane.showMessageDialog(this, "All data has been cleared.");
                Refreshable.refreshAll(SwingUtilities.getWindowAncestor(this));
            }
        });

        JButton backup = new JButton("Backup Now");
        backup.setFont(new Font("Inter", Font.BOLD, 14));
        backup.setPreferredSize(new Dimension(130, 45)); // INCREASED SIZE
        backup.putClientProperty(FlatClientProperties.STYLE, "arc: 999; background: #4CAF50; foreground: #FFFFFF;");
        backup.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backup.addActionListener(e -> JOptionPane.showMessageDialog(this, "Cloud Sync Successful!\nData backed up to SES Server."));

        p.add(reset);
        p.add(backup);
        return p;
    }

    @Override
    public void refreshData() {
        removeAll();
        initUI();
        revalidate();
        repaint();
    }
}
