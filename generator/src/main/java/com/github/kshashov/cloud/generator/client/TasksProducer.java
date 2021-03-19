package com.github.kshashov.cloud.generator.client;

import com.github.kshashov.cloud.utils.GenerateTasks;
import com.github.kshashov.cloud.utils.Task;
import com.github.kshashov.cloud.utils.TaskDto;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface TasksProducer {
    List<Task> create(@RequestBody List<TaskDto> taskDtos);

    void generate(GenerateTasks generateTasks);
}
