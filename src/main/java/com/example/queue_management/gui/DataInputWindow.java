package com.example.queue_management.gui;


import com.example.queue_management.exceptions.IllegalInputException;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DataInputWindow extends JFrame {

    private final JTextField timeLimit = new JTextField(10);
    private final JTextField maxProcessingTime = new JTextField(10);
    private final JTextField minProcessingTime = new JTextField(10);
    private final JTextField maxArrivalTime = new JTextField(10);
    private final JTextField minArrivalTime = new JTextField(10);
    private final JTextField numberOfServers = new JTextField(10);
    private final JTextField numberOfClients = new JTextField(10);
    private boolean simulationNotStarted = true;

    public DataInputWindow() {

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Container inputLabels = makeItLeft();
        Container inputTextBox = makeItRight();
        Container contentPane = this.getContentPane();
        contentPane.setLayout(new FlowLayout());
        contentPane.add(inputLabels);
        contentPane.add(inputTextBox);
        JButton startBtn = new JButton("Start simulation");
        startBtn.addActionListener(event -> {
            List<Integer> inputs;
            try {
                inputs = getInputs();
                checkInput(inputs);
            } catch (IllegalInputException e) {
                return;
            }
            SwingUtilities.getRoot((Component) event.getSource()).setVisible(false); // hide frame
            simulationNotStarted = false;
        });
        contentPane.add(startBtn);

        this.pack();
        this.setVisible(true);

    }

    private static Container makeItLeft() {
        JPanel container = new JPanel();
        BoxLayout layout = new BoxLayout(container, BoxLayout.Y_AXIS);
        container.setLayout(layout);
        JLabel btn1 = new JLabel("Simulation interval");
        btn1.setAlignmentX(Component.LEFT_ALIGNMENT);
        container.add(btn1);
        JLabel btn2 = new JLabel("Minimum arrival time");
        btn2.setAlignmentX(Component.LEFT_ALIGNMENT);
        container.add(btn2);
        JLabel btn3 = new JLabel("Maximum arrival time");
        btn3.setAlignmentX(Component.LEFT_ALIGNMENT);
        container.add(btn3);
        JLabel btn4 = new JLabel("Minimum service time");
        btn4.setAlignmentX(Component.LEFT_ALIGNMENT);
        container.add(btn4);
        JLabel btn5 = new JLabel("Maximum service time");
        btn5.setAlignmentX(Component.LEFT_ALIGNMENT);
        container.add(btn5);
        JLabel btn6 = new JLabel("Number of queues");
        btn6.setAlignmentX(Component.LEFT_ALIGNMENT);
        container.add(btn6);
        JLabel btn7 = new JLabel("Number of clients");
        btn7.setAlignmentX(Component.LEFT_ALIGNMENT);
        container.add(btn7);

        return container;
    }

    private Container makeItRight() {
        JPanel container = new JPanel();
        BoxLayout layout = new BoxLayout(container, BoxLayout.Y_AXIS);
        container.setLayout(layout);
        timeLimit.setAlignmentX(Component.RIGHT_ALIGNMENT);
        container.add(timeLimit);
        minArrivalTime.setAlignmentX(Component.RIGHT_ALIGNMENT);
        container.add(minArrivalTime);
        maxArrivalTime.setAlignmentX(Component.RIGHT_ALIGNMENT);
        container.add(maxArrivalTime);
        minProcessingTime.setAlignmentX(Component.RIGHT_ALIGNMENT);
        container.add(minProcessingTime);
        maxProcessingTime.setAlignmentX(Component.RIGHT_ALIGNMENT);
        container.add(maxProcessingTime);
        numberOfServers.setAlignmentX(Component.RIGHT_ALIGNMENT);
        container.add(numberOfServers);
        numberOfClients.setAlignmentX(Component.RIGHT_ALIGNMENT);
        container.add(numberOfClients);

        return container;
    }

    public List<Integer> getInputs() {
        List<Integer> inputs = new ArrayList<>();
        try {
            inputs.add(Integer.parseInt(timeLimit.getText().trim()));
            inputs.add(Integer.parseInt(minArrivalTime.getText().trim()));
            inputs.add(Integer.parseInt(maxArrivalTime.getText().trim()));
            inputs.add(Integer.parseInt(minProcessingTime.getText().trim()));
            inputs.add(Integer.parseInt(maxProcessingTime.getText().trim()));
            inputs.add(Integer.parseInt(numberOfServers.getText().trim()));
            inputs.add(Integer.parseInt(numberOfClients.getText().trim()));
        } catch (NumberFormatException e) {
            throw new IllegalInputException(this, "All inputs must be positive integers!");
        }
        return inputs;
    }

    private void checkInput(List<Integer> inputs) {
        if (inputs.isEmpty()) {
            throw new IllegalInputException(this, "Please fill all the input boxes");
        } else if (inputs.get(1) > inputs.get(2) || inputs.get(3) > inputs.get(4)) {
            throw new IllegalInputException(this, "min arrival time and min service time must be smaller then max inputs!");
        } else {
            for (int input : inputs) {
                if (input <= 0) {
                    throw new IllegalInputException(this, "Inputs must be positive integers!");
                }
            }
        }
    }

    public boolean isSimulationNotStarted() {
        return simulationNotStarted;
    }
}