package com.github.kshashov.cloud.producer.services;

import com.github.kshashov.cloud.utils.Task;
import com.github.kshashov.cloud.utils.TaskDto;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TaskRegistry {

    public Task register(TaskDto task) {
        // TODO
        return new Task(UUID.randomUUID(), task.getType(), task.getProperties());
    }
}
