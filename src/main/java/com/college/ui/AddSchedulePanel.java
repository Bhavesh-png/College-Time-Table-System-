package com.college.ui;

import com.college.model.TimetableEntry;
import com.college.service.TimetableService;
import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import java.awt.*;

public class AddSchedulePanel extends JPanel implements Refreshable {
    private JTextField subjectField, roomField, facultyField;
    private JComboBox<String> dayCombo, startCombo, endCombo;

    public AddSchedulePanel() {
        initUI();
    }

    private void initUI() {
        setLayout(new GridBagLayout());
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(UIManager.getColor("Panel.background"));
        card.putClientProperty(FlatClientProperties.STYLE, "arc: 45;");
        card.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
        card.setPreferredSize(new Dimension(680, 650));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        JLabel title = new JLabel("Create New Schedule");
        title.setFont(new Font("Inter", Font.BOLD, 28));
        gbc.gridwidth = 2; gbc.gridy = 0;
        card.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1; card.add(createLabel("Subject Name"), gbc);
        gbc.gridy = 2; subjectField = createField("e.g. Computer Science"); card.add(subjectField, gbc);

        gbc.gridy = 1; gbc.gridx = 1; card.add(createLabel("Facility/Room"), gbc);
        gbc.gridy = 2; roomField = createField("e.g. Hall 101"); card.add(roomField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; card.add(createLabel("Day of Week"), gbc);
        gbc.gridy = 4; dayCombo = new JComboBox<>(new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"});
        styleCombo(dayCombo); card.add(dayCombo, gbc);

        gbc.gridx = 1; gbc.gridy = 3; card.add(createLabel("Faculty/Teacher"), gbc);
        gbc.gridy = 4; facultyField = createField("e.g. Prof. Smith"); card.add(facultyField, gbc);

        // Time Selection Row
        JPanel timeRow = new JPanel(new GridLayout(1, 2, 15, 0));
        timeRow.setOpaque(false);
        
        JPanel startPart = new JPanel(new BorderLayout(0, 5));
        startPart.setOpaque(false);
        startPart.add(createLabel("Start Time"), BorderLayout.NORTH);
        startCombo = new JComboBox<>(getTimeOptions());
        styleCombo(startCombo);
        startPart.add(startCombo, BorderLayout.CENTER);

        JPanel endPart = new JPanel(new BorderLayout(0, 5));
        endPart.setOpaque(false);
        endPart.add(createLabel("End Time"), BorderLayout.NORTH);
        endCombo = new JComboBox<>(getTimeOptions());
        styleCombo(endCombo);
        endCombo.setSelectedIndex(1); // Default to 1 hour later
        endPart.add(endCombo, BorderLayout.CENTER);

        timeRow.add(startPart);
        timeRow.add(endPart);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 10, 15, 10);
        card.add(timeRow, gbc);

        gbc.gridy = 6; gbc.insets = new Insets(40, 10, 0, 10);
        JButton saveBtn = new JButton("Save Schedule");
        saveBtn.setFont(new Font("Inter", Font.BOLD, 16));
        saveBtn.putClientProperty(FlatClientProperties.STYLE, "arc: 999; background: #1976D2; foreground: #FFFFFF;");
        saveBtn.setPreferredSize(new Dimension(0, 50));
        saveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveBtn.addActionListener(e -> handleSave());
        card.add(saveBtn, gbc);

        add(card);
    }

    private String[] getTimeOptions() {
        return new String[]{"08:00 AM", "09:00 AM", "10:00 AM", "11:00 AM", "12:00 PM", "01:00 PM", "02:00 PM", "03:00 PM", "04:00 PM", "05:00 PM"};
    }

    private JLabel createLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Inter", Font.BOLD, 14));
        l.setForeground(Color.GRAY);
        return l;
    }

    private JTextField createField(String p) {
        JTextField f = new JTextField();
        f.setFont(new Font("Inter", Font.PLAIN, 15));
        f.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, p);
        f.putClientProperty(FlatClientProperties.STYLE, "arc: 15;");
        f.setPreferredSize(new Dimension(280, 45));
        return f;
    }

    private void styleCombo(JComboBox<?> c) {
        c.setFont(new Font("Inter", Font.PLAIN, 15));
        c.putClientProperty(FlatClientProperties.STYLE, "arc: 15;");
        c.setPreferredSize(new Dimension(280, 45));
    }

    private void handleSave() {
        if (subjectField.getText().trim().isEmpty() || roomField.getText().trim().isEmpty() || facultyField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields!", "Missing Info", JOptionPane.WARNING_MESSAGE);
            return;
        }

        TimetableEntry e = new TimetableEntry(
            (String)dayCombo.getSelectedItem(),
            (String)startCombo.getSelectedItem(),
            (String)endCombo.getSelectedItem(),
            subjectField.getText().trim(),
            facultyField.getText().trim(),
            roomField.getText().trim()
        );
        
        if (TimetableService.addEntry(e)) {
            JOptionPane.showMessageDialog(this, "Schedule added successfully!");
            subjectField.setText("");
            roomField.setText("");
            facultyField.setText("");
            Refreshable.refreshAll(SwingUtilities.getWindowAncestor(this));
        } else {
            JOptionPane.showMessageDialog(this, "Conflict detected! This slot is already occupied.", "Scheduling Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void refreshData() {}
}
