package com.github.kshashov.cloud.task;

import com.github.kshashov.cloud.task.client.TasksProducer;
import com.github.kshashov.cloud.utils.Task;
import com.github.kshashov.cloud.utils.TaskDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;

import java.util.HashMap;
import java.util.List;

@Component
public class ProduceTaskCommandLineRunner implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(ProduceTaskCommandLineRunner.class);
    private final TasksProducer tasksProducer;

    @Autowired
    public ProduceTaskCommandLineRunner(TasksProducer tasksProducer) {
        this.tasksProducer = tasksProducer;
    }

    @Override
    public void run(String... args) {
        TaskDto task1 = new TaskDto("sum", new HashMap<>());
        TaskDto task2 = new TaskDto("", new HashMap<>());

        try {
            List<Task> result = tasksProducer.create(List.of(task1, task2));
            if (result != null) {
                result.forEach(t -> log.info("task:" + t.toString()));
            }
        } catch (HttpServerErrorException serverErrorException) {
            log.error(serverErrorException.getStatusCode().value() + ": " + serverErrorException.getResponseBodyAsString());
        }
    }
}
