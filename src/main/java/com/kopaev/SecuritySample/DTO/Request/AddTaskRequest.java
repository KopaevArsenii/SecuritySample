package com.kopaev.SecuritySample.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddTaskRequest {
    private String description;
    private boolean isDone;
    private String name;
}
