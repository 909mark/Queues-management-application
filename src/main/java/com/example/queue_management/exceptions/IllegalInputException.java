package com.example.queue_management.exceptions;

import javax.swing.*;

public class IllegalInputException extends RuntimeException {

    public IllegalInputException(JFrame frame, String msg) {
        JOptionPane.showMessageDialog(frame,
                msg,
                "Incorrect input entered!",
                JOptionPane.WARNING_MESSAGE);
    }
}
