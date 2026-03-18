package com.college.ui.components;

import com.college.model.TimetableEntry;
import com.college.service.TimetableService;
import com.college.ui.Refreshable;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class TimetableGrid extends JComponent {
    private static final String[] DAYS = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    private static final String[] DAYS_DISPLAY = {"MON 16", "TUE 17", "WED 18", "THU 19", "FRI 20", "SAT 21"};
    
    public TimetableGrid() {
        setOpaque(false);
        setToolTipText("");
        setupContextMenu();
    }

    private void setupContextMenu() {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem deleteItem = new JMenuItem("Remove Schedule");
        deleteItem.setFont(new Font("Inter", Font.BOLD, 12));
        deleteItem.addActionListener(e -> {
            Point p = getMousePosition();
            if (p != null) {
                TimetableEntry ent = findEntryAt(p.x, p.y);
                if (ent != null) {
                    int res = JOptionPane.showConfirmDialog(this, "Delete session: " + ent.getSubject() + "?", "Confirm", JOptionPane.YES_NO_OPTION);
                    if (res == JOptionPane.YES_OPTION) {
                        TimetableService.getAllEntries().remove(ent);
                        TimetableService.save();
                        Refreshable.refreshAll(SwingUtilities.getWindowAncestor(this));
                    }
                }
            }
        });
        menu.add(deleteItem);
        setComponentPopupMenu(menu);
    }

    private List<String> getFaculties() {
        Set<String> set = new TreeSet<>();
        for (TimetableEntry e : TimetableService.getAllEntries()) {
            set.add(e.getFaculty());
        }
        List<String> sorted = new ArrayList<>(set);
        if (sorted.isEmpty()) sorted.add("Example Faculty");
        return sorted;
    }

    private TimetableEntry findEntryAt(int mx, int my) {
        int sidebarW = 200;
        int headerH = 80;
        List<String> faculties = getFaculties();
        int rowH = 120;
        int cellW = (getWidth() - sidebarW - 40) / DAYS.length;

        int d = (mx - sidebarW) / cellW;
        int r = (my - headerH) / rowH;

        if (d >= 0 && d < DAYS.length && r >= 0 && r < faculties.size()) {
            String faculty = faculties.get(r);
            List<TimetableEntry> entries = TimetableService.getAllEntries().stream()
                    .filter(e -> e.getFaculty().equalsIgnoreCase(faculty) && e.getDay().equalsIgnoreCase(DAYS[d]))
                    .collect(Collectors.toList());
            
            // For now, return the first one found in the cell area
            if (!entries.isEmpty()) return entries.get(0);
        }
        return null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int sidebarW = 200;
        int headerH = 80;
        List<String> faculties = getFaculties();
        int rowH = 120;
        int cellW = (w - sidebarW - 40) / DAYS.length;

        // Main Background (White card)
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, w, h, 35, 35);

        // Header Labels
        g2.setFont(new Font("Inter", Font.BOLD, 13));
        for (int i = 0; i < DAYS_DISPLAY.length; i++) {
            int x = sidebarW + i * cellW;
            g2.setColor(new Color(245, 245, 247));
            g2.fillRoundRect(x + 10, 20, cellW - 20, 40, 15, 15);
            
            g2.setColor(new Color(110, 110, 120));
            FontMetrics fm = g2.getFontMetrics();
            String day = DAYS_DISPLAY[i];
            g2.drawString(day, x + (cellW - fm.stringWidth(day))/2, 45);
        }

        // Faculty Sidebar & Grid Lines
        g2.setStroke(new BasicStroke(1.0f));
        for (int i = 0; i < faculties.size(); i++) {
            int y = headerH + i * rowH;
            
            // Divider
            g2.setColor(new Color(0, 0, 0, 15));
            g2.drawLine(20, y, w - 20, y);

            // Faculty Info
            String name = faculties.get(i);
            drawFacultyHeader(g2, 30, y + 25, name);
        }

        // Render Matrix Entries
        List<TimetableEntry> allEntries = TimetableService.getAllEntries();
        for (int r = 0; r < faculties.size(); r++) {
            String faculty = faculties.get(r);
            for (int d = 0; d < DAYS.length; d++) {
                String day = DAYS[d];
                int finalD = d;
                List<TimetableEntry> cellEntries = allEntries.stream()
                        .filter(e -> e.getFaculty().equalsIgnoreCase(faculty) && e.getDay().equalsIgnoreCase(day))
                        .collect(Collectors.toList());

                int x = sidebarW + d * cellW;
                int y = headerH + r * rowH;

                if (cellEntries.isEmpty()) {
                    g2.setFont(new Font("Inter", Font.ITALIC, 11));
                    g2.setColor(new Color(200, 200, 210));
                    g2.drawString("Available", x + 25, y + 60);
                } else {
                    int offset = 0;
                    for (TimetableEntry e : cellEntries) {
                        drawEntryChip(g2, x + 10, y + 15 + offset, cellW - 20, 85, e);
                        offset += 95; // Support stacking if multiple (though rare for same faculty)
                        if (offset > 40) break; // Limit to one visible for now
                    }
                }
            }
        }
    }

    private void drawFacultyHeader(Graphics2D g2, int x, int y, String name) {
        // Avatar
        g2.setColor(getSubjectColor(name));
        g2.fillOval(x, y, 45, 45);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Inter", Font.BOLD, 16));
        String initials = name.substring(0, 1).toUpperCase() + (name.contains(" ") ? name.split(" ")[1].substring(0,1).toUpperCase() : "");
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(initials, x + (45 - fm.stringWidth(initials))/2, y + 28);

        // Name
        g2.setColor(new Color(33, 33, 33));
        g2.setFont(new Font("Inter", Font.BOLD, 14));
        g2.drawString(name, x + 60, y + 20);
        
        // Workload
        long totalMinutes = TimetableService.getAllEntries().stream()
                .filter(e -> e.getFaculty().equalsIgnoreCase(name))
                .mapToLong(e -> 60) // Assuming 1h for now as per simple data model
                .sum();
        long hours = totalMinutes / 60;
        
        g2.setFont(new Font("Inter", Font.PLAIN, 12));
        g2.setColor(hours > 30 ? new Color(255, 82, 82) : new Color(76, 175, 80));
        g2.drawString(hours + "h / 40h", x + 60, y + 38);
    }

    private void drawEntryChip(Graphics2D g2, int x, int y, int w, int h, TimetableEntry e) {
        Color base = getSubjectColor(e.getSubject());
        Color bg = new Color(base.getRed(), base.getGreen(), base.getBlue(), 40);
        
        g2.setColor(bg);
        g2.fillRoundRect(x, y, w, h, 20, 20);
        
        g2.setColor(base);
        g2.setStroke(new BasicStroke(2.5f));
        g2.drawRoundRect(x, y, w, h, 20, 20);
        
        // Subject
        g2.setFont(new Font("Inter", Font.BOLD, 13));
        g2.drawString(e.getSubject(), x + 15, y + 28);
        
        // Time
        g2.setFont(new Font("Inter", Font.PLAIN, 12));
        g2.setColor(new Color(60, 60, 60));
        String timeRange = e.getStart() + " - " + e.getEnd();
        g2.drawString(timeRange, x + 15, y + 48);
        
        // Duration
        g2.setFont(new Font("Inter", Font.PLAIN, 11));
        g2.setColor(new Color(100, 100, 100));
        g2.drawString("(1h 00m)", x + 15, y + 65);
    }

    private Color getSubjectColor(String s) {
        int hash = Math.abs(s.hashCode());
        float h = (hash % 360) / 360f;
        // Management: Pink, Cuisine: Orange, etc.
        // We'll use HSB to keep them vibrant but light enough
        return new Color(Color.HSBtoRGB(h, 0.65f, 0.75f));
    }

    @Override
    public String getToolTipText(java.awt.event.MouseEvent e) {
        TimetableEntry ent = findEntryAt(e.getX(), e.getY());
        if (ent != null) {
            return "<html><div style='padding:8px;'><b>" + ent.getSubject() + "</b><br>" + 
                   ent.getFaculty() + "<br>Room: " + ent.getRoom() + "</div></html>";
        }
        return null;
    }
}
