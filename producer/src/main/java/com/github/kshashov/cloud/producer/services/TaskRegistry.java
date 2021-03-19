package com.github.kshashov.cloud.producer.services;

import com.github.kshashov.cloud.utils.GenerateTasks;
import com.github.kshashov.cloud.utils.Task;
import com.github.kshashov.cloud.utils.TaskDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@EnableBinding(Source.class)
public class TaskRegistry {
    private final Source source;
    private final KafkaTemplate<String, Task> kafkaTemplate;

    @Autowired
    public TaskRegistry(Source source, KafkaTemplate<String, Task> kafkaTemplate) {
        this.source = source;
        this.kafkaTemplate = kafkaTemplate;
    }

    public Task register(TaskDto dto) {
        Task task = new Task(UUID.randomUUID(), dto.getType(), dto.getProperties());
        kafkaTemplate.send(task.getType(), task);
        return task;
    }

    public void generate(int count) {
        source.output().send(new GenericMessage<>(new GenerateTasks(count)));
    }
}
