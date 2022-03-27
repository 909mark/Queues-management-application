package com.example.queue_management.controller;

import com.example.queue_management.model.Server;
import com.example.queue_management.model.Task;
import com.example.queue_management.strategy.ConcreteStrategyQueue;
import com.example.queue_management.strategy.ConcreteStrategyTime;
import com.example.queue_management.strategy.SelectionPolicy;
import com.example.queue_management.strategy.Strategy;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class Scheduler {


    private final List<Server> servers;
    private final List<Thread> threads;
    private final AtomicInteger currentTime;
    private Strategy strategy;

    public Scheduler(final int maxNoServers,final int maxTasksPerServer) {
        servers = new LinkedList<>();
        threads = new LinkedList<>();
        currentTime = new AtomicInteger(0);
        IntStream.range(0, maxNoServers).forEach(id -> servers.add(
                new Server(id,
                        new ArrayBlockingQueue<>(maxTasksPerServer),
                        new AtomicInteger(0),
                        currentTime))
        );

    }

    public void changePolicy(final SelectionPolicy policy) {
        if(policy == SelectionPolicy.SHORTEST_QUEUE) {
            strategy = new ConcreteStrategyQueue();
        }
        if(policy == SelectionPolicy.SHORTEST_TIME) {
            strategy = new ConcreteStrategyTime();
        }
    }

    synchronized void dispatchTask(final Task task) {
        strategy.addTask(servers, task);
    }

    public List<Server> getServers() {
        return servers;
    }

    public void startAll() {
        servers.forEach(server -> {
            Thread temp = new Thread(server);
            temp.setName("Thread" + server.getId());
            threads.add(temp);
            temp.start();
        });
    }

    public void killAll() {
        for(Thread t : threads)
            t.interrupt();
    }

    public String getResults() {
        List<Integer> mergedWaitingTimes = new ArrayList<>();
        List<Integer> mergedServiceTimes = new ArrayList<>();
        List<Integer> mergedPeakHours = new ArrayList<>();
        for(Server server : servers) {
            mergedWaitingTimes.addAll(server.getWaitingTimes());
            mergedServiceTimes.addAll(server.getServiceTimes());
            mergedPeakHours.add(server.getPeakHour());
        }

        StringBuilder sb = new StringBuilder("");
        OptionalDouble avg;

        avg = mergedWaitingTimes.stream().mapToDouble(o -> o).average();
        sb.append("Avg waiting time: ");
        if(avg.isPresent())
            sb.append(avg.getAsDouble());
        else
            sb.append("error");
        sb.append("\n");

        avg = mergedServiceTimes.stream().mapToDouble(o -> o).average();
        sb.append("Avg service time: ");
        if(avg.isPresent())
            sb.append(avg.getAsDouble());
        else
            sb.append("error");
        sb.append("\n");

        OptionalInt peakHour = mergedPeakHours.stream().mapToInt(v -> v).max();
        sb.append("Peak hour: ");
        if(peakHour.isPresent())
            sb.append(peakHour.getAsInt());
        else
            sb.append("error");
        return sb.toString();
    }

    public void tick() {
        currentTime.incrementAndGet();
    }

}
