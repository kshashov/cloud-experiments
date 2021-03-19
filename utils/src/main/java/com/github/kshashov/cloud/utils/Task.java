package com.github.kshashov.cloud.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task implements Serializable {
    private UUID id;
    private String type;
    private Map<String, String> properties;
}
