package com.example.queue_management.model;

public class Task {
    private final int id;
    private final int arrivalTime;
    private int serviceTime;

    public Task(int id, int arrivalTime, int serviceTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public int getTaskID() {
        return id;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void decrementServiceTime() {
        --serviceTime;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d, %d)", id, arrivalTime, serviceTime);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Task))
            return false;
        return ((Task) obj).getTaskID() == this.id;
    }

    @Override
    public int hashCode() {
        return 31 * id + 37 * arrivalTime + 39 * serviceTime;
    }
}
