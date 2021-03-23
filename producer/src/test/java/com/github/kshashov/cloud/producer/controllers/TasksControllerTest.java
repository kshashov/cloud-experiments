package com.github.kshashov.cloud.producer.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kshashov.cloud.producer.services.TaskRegistry;
import com.github.kshashov.cloud.utils.GenerateTasksEvent;
import com.github.kshashov.cloud.utils.Task;
import com.github.kshashov.cloud.utils.TaskDto;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import static com.github.kshashov.cloud.producer.TestUtils.buildKafkaConsumer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.kafka.test.utils.KafkaTestUtils.getSingleRecord;

@EnableBinding(TasksControllerTest.TaskSource.class)
@ActiveProfiles("test")
@EmbeddedKafka(topics = {"sum", "multiply"})
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}"
)
public class TasksControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private Source output;

    @Autowired
    private TaskSource taskOutput;

    @Autowired
    private MessageCollector collector;

    @SpyBean
    private TaskRegistry taskRegistry;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void generate_NoParameters() throws InterruptedException, JsonProcessingException {
        ResponseEntity<Void> result = this.restTemplate.getForEntity("http://localhost:" + port + "/api/tasks/generate", Void.class);

        GenerateTasksEvent event = validateGenerateResult(result);
        assertEquals(1, event.getCount());

        verify(taskRegistry, times(1)).generate(1);
    }

    @Test
    public void generate_WithParameter() throws InterruptedException, JsonProcessingException {
        ResponseEntity<Void> result = this.restTemplate.getForEntity("http://localhost:" + port + "/api/tasks/generate?count=3", Void.class);

        GenerateTasksEvent event = validateGenerateResult(result);
        assertEquals(3, event.getCount());

        verify(taskRegistry, times(1)).generate(3);
    }

    private GenerateTasksEvent validateGenerateResult(ResponseEntity<Void> result) throws InterruptedException, JsonProcessingException {
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());

        BlockingQueue<Message<?>> messages = collector.forChannel(output.output());
        Message<?> message = messages.poll(5, TimeUnit.SECONDS);

        assertNotNull(message);
        assertNotNull(message.getPayload());

        return new ObjectMapper().readValue((String) message.getPayload(), GenerateTasksEvent.class);
    }

    @Test
    public void add() {
        TaskDto task1 = new TaskDto("sum", Map.of("a", "1", "b", "2"));
        TaskDto task2 = new TaskDto("multiply", Map.of("a", "3", "b", "4"));

        ResponseEntity<Task[]> result = this.restTemplate.exchange("http://localhost:" + port + "/api/tasks", HttpMethod.PUT, new HttpEntity<>(List.of(task1, task2)), Task[].class);
        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());

        final Consumer<String, Task> consumer = buildKafkaConsumer(embeddedKafkaBroker, StringDeserializer.class, JsonDeserializer.class);
        embeddedKafkaBroker.consumeFromEmbeddedTopics(consumer, "sum", "multiply");
        // Check task1
        ConsumerRecord<String, Task> record = getSingleRecord(consumer, "sum", 5000);
        assertNotNull(record);
        assertNotNull(record.value());

        Task task = record.value();
        assertNotNull(task.getId());
        assertEquals("sum", task.getType());
        assertEquals("1", task.getProperties().get("a"));
        assertEquals("2", task.getProperties().get("b"));

        // Check task2
        record = getSingleRecord(consumer, "multiply", 5000);
        assertNotNull(record);
        assertNotNull(record.value());

        task = record.value();
        assertNotNull(task.getId());
        assertEquals("multiply", task.getType());
        assertEquals("3", task.getProperties().get("a"));
        assertEquals("4", task.getProperties().get("b"));

        verify(taskRegistry, times(2)).register(any(TaskDto.class));

        consumer.close();
    }

    public interface TaskSource {
        @Output("sum")
        MessageChannel sum();

        @Output("multiply")
        MessageChannel multiply();
    }
}
