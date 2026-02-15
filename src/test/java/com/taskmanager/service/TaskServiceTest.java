package com.taskmanager.service;

import com.taskmanager.dto.TaskRequestDTO;
import com.taskmanager.dto.TaskResponseDTO;
import com.taskmanager.dto.UpdateStatusDTO;
import com.taskmanager.entity.Task;
import com.taskmanager.enums.TaskStatus;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.exception.ValidationException;
import com.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private TaskRequestDTO validRequest;
    private Task savedTask;

    @BeforeEach
    void setUp() {
        validRequest = TaskRequestDTO.builder()
                .title("Valid Task Title")
                .description("Task description")
                .build();

        savedTask = Task.builder()
                .id(1L)
                .title("Valid Task Title")
                .description("Task description")
                .status(TaskStatus.OPEN)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Create with valid data should save and return DTO")
    void create_withValidData_shouldSaveAndReturnDTO() {
        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        TaskResponseDTO result = taskService.create(validRequest);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Valid Task Title", result.getTitle());
        assertEquals("Task description", result.getDescription());
        assertEquals(TaskStatus.OPEN, result.getStatus());
        assertNotNull(result.getCreatedAt());

        ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository, times(1)).save(taskCaptor.capture());
        assertEquals("Valid Task Title", taskCaptor.getValue().getTitle());
    }

    @Test
    @DisplayName("Create with invalid title should throw exception and NOT call repository.save")
    void create_withInvalidTitle_shouldThrowAndNotSave() {
        TaskRequestDTO invalidRequest = TaskRequestDTO.builder()
                .title("ab")
                .description("Description")
                .build();

        assertThrows(ValidationException.class, () -> taskService.create(invalidRequest));
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    @DisplayName("Update status with non-existent id should throw ResourceNotFoundException")
    void updateStatus_withNonExistentId_shouldThrowResourceNotFoundException() {
        Long nonExistentId = 999L;
        UpdateStatusDTO updateStatus = UpdateStatusDTO.builder()
                .status(TaskStatus.IN_PROGRESS)
                .build();

        when(taskRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                taskService.updateStatus(nonExistentId, updateStatus)
        );

        verify(taskRepository, never()).save(any(Task.class));
    }
}
