package com.github.kshashov.cloud.producer.services;

import com.github.kshashov.cloud.producer.models.TaskDto;
import com.github.kshashov.cloud.producer.models.Task;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TaskRegistry {

    public Task register(TaskDto task) {
        // TODO
        return new Task(UUID.randomUUID(), task.getType(), task.getProperties());
    }
}
