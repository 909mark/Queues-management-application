package com.example.queue_management.controller;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MyLogger {
    private final String fileName;

    MyLogger(String name) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-NN");
        fileName = name + LocalDateTime.now().format(formatter) + ".txt";
    }

    public void log(String text) {
        try (FileWriter fw = new FileWriter(fileName, true)) {
            fw.append(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
