package com.github.kshashov.cloud.generator;

import com.github.kshashov.cloud.generator.client.TasksProducer;
import com.github.kshashov.cloud.utils.GenerateTasks;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.function.Consumer;

@EnableFeignClients
@SpringBootApplication
public class GeneratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(GeneratorApplication.class, args);
	}

	@Bean
	@LoadBalanced
	RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

	@Bean
	public Consumer<GenerateTasks> generate(TasksProducer tasksProducer) {
		return tasksProducer::generate;
	}
}
