package com.aguiar.desafio_api_todolist.service;

import com.aguiar.desafio_api_todolist.dto.TaskRequestDTO;
import com.aguiar.desafio_api_todolist.dto.TaskResponseDTO;
import com.aguiar.desafio_api_todolist.entity.Task;
import com.aguiar.desafio_api_todolist.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<TaskResponseDTO> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(task -> new TaskResponseDTO(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.isCompleted()))
                .collect(Collectors.toList());
    }

    public TaskResponseDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));

        return new TaskResponseDTO(task.getId(), task.getTitle(), task.getDescription(), task.isCompleted());
    }

    public TaskResponseDTO saveTask(TaskRequestDTO dto) {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setCompleted(dto.getCompleted() != null ? dto.getCompleted() : false);

        task = taskRepository.save(task);

        return new TaskResponseDTO(task.getId(), task.getTitle(), task.getDescription(), task.isCompleted());
    }

    public TaskResponseDTO updateTask(Long id, TaskRequestDTO dto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setCompleted(dto.getCompleted() != null ? dto.getCompleted() : false);

        task = taskRepository.save(task);

        return new TaskResponseDTO(task.getId(), task.getTitle(), task.getDescription(), task.isCompleted());
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}