package com.github.kshashov.cloud.producer.controllers;

import com.github.kshashov.cloud.producer.services.TaskRegistry;
import com.github.kshashov.cloud.utils.Task;
import com.github.kshashov.cloud.utils.TaskDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping("/tasks")
public class TasksController {
    private final TaskRegistry taskRegistry;

    @Autowired
    public TasksController(TaskRegistry taskRegistry) {
        this.taskRegistry = taskRegistry;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping
    List<Task> add(@RequestBody @NotEmpty List<@Valid TaskDto> taskDtos) {
        return taskDtos.stream().map(taskRegistry::register).collect(Collectors.toList());
    }

    @GetMapping("generate")
    void generate(@RequestParam(name = "count") Optional<Integer> count) {
        taskRegistry.generate(count.orElse(1));
    }
}
