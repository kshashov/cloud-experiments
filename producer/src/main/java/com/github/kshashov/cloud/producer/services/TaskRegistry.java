package com.github.kshashov.cloud.producer.services;

import com.github.kshashov.cloud.utils.Task;
import com.github.kshashov.cloud.utils.TaskDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TaskRegistry {
    private final StreamBridge stream;

    @Autowired
    public TaskRegistry(StreamBridge stream) {
        this.stream = stream;
    }

    public Task register(TaskDto task) {
        // TODO
        return new Task(UUID.randomUUID(), task.getType(), task.getProperties());
    }

    public void generate(int count) {
        stream.send("output", count);
    }
}
