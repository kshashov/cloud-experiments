package com.github.kshashov.cloud.generator.client;

import com.github.kshashov.cloud.utils.Task;
import com.github.kshashov.cloud.utils.TaskDto;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Primary
@Profile("feign")
@Service
public class FeignClientTasksProducer extends AbstractTasksProducer {
    private final ProducerClient producerClient;

    public FeignClientTasksProducer(ProducerClient producerClient) {
        this.producerClient = producerClient;
    }

    public List<Task> create(@RequestBody List<TaskDto> taskDtos) {
        return producerClient.add(taskDtos);
    }
}
