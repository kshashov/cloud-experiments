package com.github.kshashov.cloud.generator.client;

import com.github.kshashov.cloud.utils.Task;
import com.github.kshashov.cloud.utils.TaskDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class RestTemplateTasksProducer extends AbstractTasksProducer {
    private final RestTemplate restTemplate;

    public RestTemplateTasksProducer(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Task> create(@RequestBody List<TaskDto> taskDtos) {
        ResponseEntity<Task[]> result = restTemplate
                .exchange("http://producer/api/tasks", HttpMethod.PUT,
                        new HttpEntity<>(taskDtos), Task[].class);
        if (result.getStatusCode().equals(HttpStatus.CREATED)) {
            return Arrays.asList(Objects.requireNonNull(result.getBody()));
        }

        throw new IllegalStateException();
    }
}
