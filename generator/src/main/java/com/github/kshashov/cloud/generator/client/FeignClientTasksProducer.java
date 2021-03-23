package com.github.kshashov.cloud.generator.client;

import com.github.kshashov.cloud.utils.GenerateTasksEvent;
import com.github.kshashov.cloud.utils.Task;
import com.github.kshashov.cloud.utils.TaskDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Profile("feign")
@Service
public class FeignClientTasksProducer implements TasksProducer {
    private final static Logger log = LoggerFactory.getLogger(FeignClientTasksProducer.class);
    private final ProducerClient producerClient;

    public FeignClientTasksProducer(ProducerClient producerClient) {
        this.producerClient = producerClient;

        /*
        Resilience4 example:

                ProducerClient requestFailedFallback = list -> new ArrayList<>();
        ProducerClient circuitBreakerFallback = (list) -> new ArrayList<>();
        RateLimiter rateLimiter = RateLimiter.ofDefaults("generator");
        CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("generator");
        FeignDecorators decorators = FeignDecorators.builder()
                .withRateLimiter(rateLimiter)
                .withCircuitBreaker(circuitBreaker)
                .withFallback(requestFailedFallback, FeignException.class)
                .withFallback(circuitBreakerFallback, CallNotPermittedException.class)
                .build();

        producerClient = Resilience4jFeign.builder(decorators).target(ProducerClient.class, producerUrl);
         */
    }

    public List<Task> create(@RequestBody List<TaskDto> taskDtos) {
        return producerClient.add(taskDtos);
    }

    @Override
    public void generate(GenerateTasksEvent generateTasks) {
        List<TaskDto> tasks = new ArrayList<>();
        for (int i = 0; i < generateTasks.getCount(); i++) {
            HashMap<String, String> properties = new HashMap<>();
            properties.put("a", String.valueOf((int) (Math.random() * 10)));
            properties.put("b", String.valueOf((int) (Math.random() * 10)));
            tasks.add(new TaskDto("sum", properties));
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
