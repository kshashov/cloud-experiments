package com.github.kshashov.cloud.generator;

import com.github.kshashov.cloud.generator.client.TasksProducer;
import com.github.kshashov.cloud.utils.GenerateTasksEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class GeneratorListenerConfiguration {

    @Bean
    public Consumer<GenerateTasksEvent> generate(TasksProducer tasksProducer) {
        return tasksProducer::generate;
    }
}
