package com.github.kshashov.cloud.generator.client;

import com.github.kshashov.cloud.utils.Task;
import com.github.kshashov.cloud.utils.TaskDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

// Set url for test purposes
@FeignClient(value = "producer", url = "${producer.url}")
public interface ProducerClient {

    @PutMapping("api/tasks")
    List<Task> add(@RequestBody List<TaskDto> taskDtos);
}
