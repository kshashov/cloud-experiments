package com.github.kshashov.cloud.producer.models;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.UUID;

@Data
public class Task {
    private final @NotNull UUID id;
    private final @NotBlank String type;
    private final @NotNull Map<String, String> properties;
}
