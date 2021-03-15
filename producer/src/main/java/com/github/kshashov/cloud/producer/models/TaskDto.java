package com.github.kshashov.cloud.producer.models;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
public class TaskDto {
    private final @NotBlank String type;
    private final @NotNull Map<String, String> properties;
}
