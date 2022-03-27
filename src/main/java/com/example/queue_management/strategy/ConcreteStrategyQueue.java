package com.example.queue_management.strategy;

import com.example.queue_management.model.Server;
import com.example.queue_management.model.Task;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ConcreteStrategyQueue implements Strategy {
    @Override
    public void addTask(List<Server> servers, Task task) {
        Optional<Server> serverOptional = servers.stream().min(Comparator.comparingInt(Server::getCurrentTasksNumber));
        serverOptional.ifPresent(server -> server.addTask(task));
    }

}
