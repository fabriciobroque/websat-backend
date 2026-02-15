package com.taskmanager.service;

import com.taskmanager.dto.TaskRequestDTO;
import com.taskmanager.dto.TaskResponseDTO;
import com.taskmanager.dto.UpdateStatusDTO;
import com.taskmanager.entity.Task;
import com.taskmanager.enums.TaskStatus;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.exception.ValidationException;
import com.taskmanager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public List<TaskResponseDTO> findAll() {
        return taskRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public TaskResponseDTO findById(Long id) {
        Task task = getTaskOrThrow(id);
        return toResponseDTO(task);
    }

    @Transactional
    public TaskResponseDTO create(TaskRequestDTO request) {
        if (request.getTitle() == null || request.getTitle().trim().length() < 3) {
            throw new ValidationException("Title must have at least 3 characters");
        }
        Task task = Task.builder()
                .title(request.getTitle().trim())
                .description(request.getDescription() != null ? request.getDescription().trim() : null)
                .status(TaskStatus.OPEN)
                .build();
        task = taskRepository.save(task);
        return toResponseDTO(task);
    }

    @Transactional
    public TaskResponseDTO updateStatus(Long id, UpdateStatusDTO request) {
        Task task = getTaskOrThrow(id);
        task.setStatus(request.getStatus());
        task = taskRepository.save(task);
        return toResponseDTO(task);
    }

    @Transactional
    public void delete(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task", id);
        }
        taskRepository.deleteById(id);
    }

    private Task getTaskOrThrow(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", id));
    }

    private TaskResponseDTO toResponseDTO(Task task) {
        return TaskResponseDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .createdAt(task.getCreatedAt())
                .build();
    }
}
