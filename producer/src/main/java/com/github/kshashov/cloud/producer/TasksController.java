package com.github.kshashov.cloud.producer;

import com.github.kshashov.cloud.producer.models.TaskDto;
import com.github.kshashov.cloud.producer.models.Task;
import com.github.kshashov.cloud.producer.services.TaskRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@ControllerAdvice
@RequestMapping("/api/tasks")
public class TasksController {
    private final TaskRegistry taskRegistry;

    @Autowired
    public TasksController(TaskRegistry taskRegistry) {
        this.taskRegistry = taskRegistry;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping
    List<Task> add(@RequestBody @Valid @NotNull List<TaskDto> taskDtos) {
        return taskDtos.stream().map(taskRegistry::register).collect(Collectors.toList());
    }


}
