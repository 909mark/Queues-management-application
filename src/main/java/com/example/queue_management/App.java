package com.example.queue_management;

import com.example.queue_management.controller.SimulationManager;
import com.example.queue_management.gui.DataInputWindow;
import com.example.queue_management.gui.SimulationFrame;

public class App {
    public static void main(String[] args) throws InterruptedException {
        DataInputWindow setup = new DataInputWindow();
        while (setup.isSimulationNotStarted()) {
            Thread.sleep(100);
        }
        SimulationFrame frame = new SimulationFrame();
        Thread.sleep(1000);
        SimulationManager simulation = new SimulationManager(setup.getInputs(), frame);
        Thread t = new Thread(simulation);
        t.start();
        t.join();
    }
}
