package com.college.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Window;

public interface Refreshable {
    void refreshData();
    
    // Static helper to refresh all components in a window
    static void refreshAll(Window window) {
        if (window == null) return;
        refreshComponents(window);
    }
    
    private static void refreshComponents(Container container) {
        for (Component c : container.getComponents()) {
            if (c instanceof Refreshable) {
                ((Refreshable) c).refreshData();
            }
            if (c instanceof Container) {
                refreshComponents((Container) c);
            }
        }
    }
}
