package com.aguiar.desafio_api_todolist.service;

import com.aguiar.desafio_api_todolist.entity.Task;
import com.aguiar.desafio_api_todolist.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveListarTarefas() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Tarefa 1");

        when(taskRepository.findAll()).thenReturn(List.of(task));

        List<Task> tarefas = taskService.getAllTasks();

        assertEquals(1, tarefas.size());
        assertEquals("Tarefa 1", tarefas.get(0).getTitle());
    }

    @Test
    void deveSalvarTarefa() {
        Task task = new Task();
        task.setTitle("Nova");

        Task salva = new Task();
        salva.setId(1L);
        salva.setTitle("Nova");

        when(taskRepository.save(task)).thenReturn(salva);

        Task result = taskService.saveTask(task);

        assertNotNull(result.getId());
        assertEquals("Nova", result.getTitle());
    }

    @Test
    void deveBuscarPorId() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Buscar");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Optional<Task> resultado = taskService.getTaskById(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Buscar", resultado.get().getTitle());
    }

    @Test
    void deveAtualizarTarefa() {
        Task existente = new Task();
        existente.setId(1L);
        existente.setTitle("Antigo");

        Task atualizado = new Task();
        atualizado.setTitle("Novo");
        atualizado.setCompleted(true);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(taskRepository.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));

        Task result = taskService.updateTask(1L, atualizado);

        assertEquals("Novo", result.getTitle());
        assertTrue(result.isCompleted());
    }

    @Test
    void deveDeletarTarefa() {
        doNothing().when(taskRepository).deleteById(1L);

        assertDoesNotThrow(() -> taskService.deleteTask(1L));
        verify(taskRepository, times(1)).deleteById(1L);
    }
}