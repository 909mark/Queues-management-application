package com.example.queue_management.controller;

import com.example.queue_management.gui.SimulationFrame;
import com.example.queue_management.model.Server;
import com.example.queue_management.model.Task;
import com.example.queue_management.strategy.SelectionPolicy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class SimulationManager implements Runnable {
    protected SimulationFrame simulationFrame;
    Random random = new Random();
    public final MyLogger logger;
    private final int timeLimit;
    private final int maxProcessingTime;
    private final int minProcessingTime;
    private final int maxArrivalTime;
    private final int minArrivalTime;
    private final int numberOfServers;
    private final int numberOfClients;
    public SelectionPolicy selectionPolicy = SelectionPolicy.SHORTEST_TIME;
    private final Scheduler scheduler;
    private final List<Task> generatedTasks;

    public SimulationManager(List<Integer> inputs, SimulationFrame simulationFrame) {
        this.simulationFrame = simulationFrame;
        this.timeLimit = inputs.get(0);
        this.minArrivalTime = inputs.get(1);
        this.maxArrivalTime = inputs.get(2);
        this.minProcessingTime = inputs.get(3);
        this.maxProcessingTime = inputs.get(4);
        this.numberOfServers = inputs.get(5);
        this.numberOfClients = inputs.get(6);
        this.logger = new MyLogger("logfile");
        this.scheduler = new Scheduler(numberOfServers, numberOfClients);
        this.scheduler.changePolicy(SelectionPolicy.SHORTEST_TIME);
        this.generatedTasks = generateNRandomTasks();
    }

    private List<Task> generateNRandomTasks() {
        List<Task> tempTasks = new ArrayList<>();
        IntStream.range(0, numberOfClients).forEach(o -> tempTasks.add(new Task(o,
                random.nextInt(minArrivalTime, maxArrivalTime + 1),
                random.nextInt(minProcessingTime, maxProcessingTime + 1))));
        tempTasks.sort(Comparator.comparing(Task::getArrivalTime).thenComparing(Task::getTaskID));
        return tempTasks;
    }

    private String getStatus(int currentTime) {

        StringBuilder sb = new StringBuilder("Time " + currentTime + "\n");
        sb.append("Waiting clients: ");
        for (Task task : generatedTasks) {
            sb.append(task).append(";");
        }
        sb.deleteCharAt(sb.length()-1).append("\n");
        for (Server server : scheduler.getServers())
            sb.append(server);
        return sb.toString();
    }

    @Override
    public void run() {
        int currentTime = 0;
        synchronized (this) {
            scheduler.startAll();
        }
        try {
            while (currentTime <= timeLimit) {
                for (int i = 0; i < generatedTasks.size(); ++i) {
                    Task currentTask = generatedTasks.get(i);
                    if (currentTask.getArrivalTime() == currentTime) {
                        scheduler.dispatchTask(currentTask);
                        if (generatedTasks.remove(currentTask)) {
                            --i;
                        }
                    }
                }
                String status = getStatus(currentTime);
                simulationFrame.updateTextArea(status);
                logger.log(status);
                scheduler.tick();
                currentTime++;
                TimeUnit.SECONDS.sleep(1);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        synchronized (this) { // get result only after stopping all the threads
            scheduler.killAll();
        }
        simulationFrame.updateTextArea(scheduler.getResults());
        logger.log(scheduler.getResults());
        Thread.currentThread().interrupt();
    }

}
