package com.example.queue_management.gui;

import javax.swing.*;
import java.awt.*;

public class SimulationFrame extends JFrame {
    private final JTextArea simulationText;

    public SimulationFrame() {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        simulationText = new JTextArea(20, 80);
        simulationText.setLineWrap(true);
        simulationText.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(simulationText,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        Container container = this.getContentPane();
        container.setLayout(new FlowLayout());
        container.add(scrollPane);
        this.pack();
        this.setSize(new Dimension(900, 400));
        this.setResizable(false);
        this.setVisible(true);
    }

    public void updateTextArea(String status) {
        simulationText.setText(status);
    }
}
