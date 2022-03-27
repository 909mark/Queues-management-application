package com.example.queue_management.strategy;

import com.example.queue_management.model.Server;
import com.example.queue_management.model.Task;

import java.util.List;

public interface Strategy {

    void addTask(List<Server> servers, Task task);

}
