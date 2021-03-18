package com.github.kshashov.cloud.generator.client;

import com.github.kshashov.cloud.utils.Task;
import com.github.kshashov.cloud.utils.TaskDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class AbstractTasksProducer implements TasksProducer {
    private final static Logger log = LoggerFactory.getLogger(AbstractTasksProducer.class);

    @Override
    public void generate(int count) {
        List<TaskDto> tasks = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            tasks.add(new TaskDto("sum", new HashMap<>()));
        }

        try {
            List<Task> result = create(tasks);
            if (result != null) {
                result.forEach(t -> log.info("task:" + t.toString()));
            }
        } catch (HttpServerErrorException serverErrorException) {
            log.error(serverErrorException.getStatusCode().value() + ": " + serverErrorException.getResponseBodyAsString());
        }
    }
}
