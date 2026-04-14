package com.aguiar.desafio_api_todolist.controller;

import com.aguiar.desafio_api_todolist.dto.TaskRequestDTO;
import com.aguiar.desafio_api_todolist.dto.TaskResponseDTO;
import com.aguiar.desafio_api_todolist.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tarefas", description = "Endpoints para gerenciar a lista de tarefas")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }

    @Operation(summary = "Lista todas as tarefas")
    @GetMapping
    public List<TaskResponseDTO> getAllTasks(){
        return taskService.getAllTasks();
    }

    @Operation(summary = "Busca uma tarefa por ID")
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Long id){
        try {
            TaskResponseDTO task = taskService.getTaskById(id);
            return ResponseEntity.ok(task);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Cria uma nova tarefa")
    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@Valid @RequestBody TaskRequestDTO taskRequestDTO){
        TaskResponseDTO created = taskService.saveTask(taskRequestDTO);
        return ResponseEntity.status(201).body(created);
    }

    @Operation(summary = "Atualiza uma tarefa existente")
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable Long id, @Valid @RequestBody TaskRequestDTO taskRequestDTO){
        try {
            TaskResponseDTO updated = taskService.updateTask(id, taskRequestDTO);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Remove uma tarefa pelo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id){
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}