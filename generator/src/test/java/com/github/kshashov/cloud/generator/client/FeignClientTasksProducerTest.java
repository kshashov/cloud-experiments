package com.github.kshashov.cloud.generator.client;

import com.github.kshashov.cloud.generator.Mocks;
import com.github.kshashov.cloud.generator.WireMockConfig;
import com.github.kshashov.cloud.utils.GenerateTasksEvent;
import com.github.kshashov.cloud.utils.Task;
import com.github.kshashov.cloud.utils.TaskDto;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@SpringBootTest
@ActiveProfiles({"test", "feign"})
@ContextConfiguration(classes = {WireMockConfig.class})
public class FeignClientTasksProducerTest {

    @Autowired
    ProducerClient producerClient;

    @Autowired
    private WireMockServer mockBooksService;

    @Captor
    ArgumentCaptor<List<TaskDto>> argCaptor;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_RequestIsSent() {
        FeignClientTasksProducer producer = spy(new FeignClientTasksProducer(producerClient));

        Mocks.producerAdd(mockBooksService, "[{" +
                "\"id\": \"4d410172-45de-4ed1-a85c-8238adf9d644\", \"type\": \"sum\", \"properties\": {\"a\": \"1\", \"b\": \"2\"}" +
                "}, {" +
                "\"id\": \"4d410172-45de-4ed1-a85c-8238adf9d644\", \"type\": \"multiply\", \"properties\": {\"a\": \"3\", \"b\": \"4\"}" +
                "}]");

        TaskDto task1 = new TaskDto("sum", Map.of("a", "1", "b", "2"));
        TaskDto task2 = new TaskDto("multiply", Map.of("a", "3", "b", "4"));
        List<TaskDto> tasks = List.of(task1, task2);

        List<Task> result = producer.create(tasks);

        // Check result tasks
        assertNotNull(result);
        assertEquals(2, result.size());

        int i = 0;
        for (TaskDto task : tasks) {
            assertEquals(task.getType(), result.get(i).getType(), "Task types should be equals");
            assertEquals(task.getProperties(), result.get(i).getProperties(), "Task parameters should be equals");
            assertNotNull(result.get(i).getId(), "Task id should not be null" + UUID.randomUUID());
            i++;
        }
    }

    @Test
    void generate_InvokeCreate() {
        FeignClientTasksProducer producer = spy(new FeignClientTasksProducer(producerClient));
        doReturn(new ArrayList<>()).when(producer).create(argCaptor.capture());

        producer.generate(new GenerateTasksEvent(3));

        assertNotNull(argCaptor.getValue());
        assertEquals(3, argCaptor.getValue().size());
    }
}
