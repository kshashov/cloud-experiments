package com.github.kshashov.cloud.task.client;

import com.github.kshashov.cloud.utils.Task;
import com.github.kshashov.cloud.utils.TaskDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("producer")
public interface ProducerClient {

    @PutMapping("api/tasks")
    List<Task> add(@RequestBody List<TaskDto> taskDtos);
}
