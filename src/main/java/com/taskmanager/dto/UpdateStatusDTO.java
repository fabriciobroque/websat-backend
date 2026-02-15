package com.taskmanager.dto;

import com.taskmanager.enums.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStatusDTO {

    @NotNull(message = "Status is required")
    private TaskStatus status;
}
