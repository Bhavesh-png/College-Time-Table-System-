package com.college.ui.components;

import javax.swing.*;
import com.formdev.flatlaf.FlatClientProperties;

public class RoundedTextField extends JTextField {
    public RoundedTextField(int columns, String placeholder) {
        super(columns);
        putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, placeholder);
        putClientProperty(FlatClientProperties.STYLE, "arc: 12;");
    }

    public RoundedTextField(String placeholder) {
        this(0, placeholder);
    }
}
