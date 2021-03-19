package com.github.kshashov.cloud.sum;

import com.github.kshashov.cloud.utils.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class SumTaskListener {
    private static final Logger log = LoggerFactory.getLogger(SumTaskListener.class);

    @KafkaListener(topics = "sum", groupId = "process")
    public void sum(@Payload Task task) {
        int a = Integer.parseInt(task.getProperties().get("a"));
        int b = Integer.parseInt(task.getProperties().get("b"));
        log.info(a + " + " + b + " = " + (a + b));
    }
}
