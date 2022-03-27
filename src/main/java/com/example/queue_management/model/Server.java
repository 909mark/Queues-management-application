package com.example.queue_management.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private int peakHourQueueSize;
    private int peakHour;
    private final int id;
    private final AtomicInteger currentTime;
    private final AtomicInteger waitingPeriod; // why is this atomic?
    private final BlockingQueue<Task> tasks;
    private final List<Integer> waitingTimes;
    private final List<Integer> serviceTimes;

    public Server(int id,
                  BlockingQueue<Task> tasks,
                  AtomicInteger waitingPeriod,
                  AtomicInteger currentTime) {
        this.id = id;
        this.tasks = tasks;
        this.waitingPeriod = waitingPeriod;
        this.currentTime = currentTime;
        this.waitingTimes = new LinkedList<>();
        this.serviceTimes = new LinkedList<>();
    }

    public void addTask(Task newTask) {
        if (tasks.offer(newTask)) {
            serviceTimes.add(newTask.getServiceTime());
            waitingTimes.add(newTask.getServiceTime() + waitingPeriod.get());
            waitingPeriod.addAndGet(newTask.getServiceTime());
        } else {
            throw new IllegalStateException();
        }
    }

    public int getWaitingPeriod() {
        return waitingPeriod.get();
    }

    public int getCurrentTasksNumber() {
        return tasks.size();
    }

    public int getId() {
        return id;
    }

    public int getPeakHour() {
        return peakHour;
    }

    public List<Integer> getWaitingTimes() {
        return waitingTimes;
    }

    public List<Integer> getServiceTimes() {
        return serviceTimes;
    }

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {

        try {
            while (true) {
                TimeUnit.SECONDS.sleep(1);
                Task headTask = tasks.peek();
                if (headTask != null) {
                     // don't increment service time immediately, wait 1 second
                    if (tasks.size() > peakHourQueueSize) { // checking peak hour
                        peakHour = currentTime.get() - 1; // server time is always 1 second delayed compared to scheduler
                        peakHourQueueSize = tasks.size();
                    }
                    if (headTask.getServiceTime() > 1) {
                        headTask.decrementServiceTime();
                    } else {
                        tasks.poll();
                    }
                    waitingPeriod.decrementAndGet();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(String.format("Queue %d:", id));
        if (tasks.isEmpty()) {
            sb.append("closed\n");
            return sb.toString();
        }
        for (Task task : tasks) {
            sb.append(String.format("(%3d, %2d, %2d); ", task.getTaskID(), task.getArrivalTime(), task.getServiceTime()));
        }
        return sb.deleteCharAt(sb.length() - 1).append("\n").toString();
    }

}

