package com.github.kshashov.cloud.sum;

import com.github.kshashov.cloud.utils.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;

@SpringBootApplication
public class SumApplication {

    public static void main(String[] args) {
        SpringApplication.run(SumApplication.class, args);
    }

    @Bean
    SumTaskListener sumTaskListener() {
        return new SumTaskListener();
    }

    public static class SumTaskListener {
        private static final Logger log = LoggerFactory.getLogger(SumTaskListener.class);

        @KafkaListener(topics = "sum", groupId = "process")
        public void sum(@Payload Task task) {
            int a = Integer.parseInt(task.getProperties().get("a"));
            int b = Integer.parseInt(task.getProperties().get("b"));
            log.info(a + " + " + b + " = " + (a + b));
        }
    }
}
