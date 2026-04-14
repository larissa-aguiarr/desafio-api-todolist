package com.aguiar.desafio_api_todolist.controller;

import com.aguiar.desafio_api_todolist.entity.Task;
import com.aguiar.desafio_api_todolist.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveRetornarTodasAsTarefas() throws Exception {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Tarefa");

        Mockito.when(taskService.getAllTasks()).thenReturn(List.of(task));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Tarefa"));
    }

    @Test
    void deveRetornarTarefaPorId() throws Exception {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Buscar");

        Mockito.when(taskService.getTaskById(1L)).thenReturn(Optional.of(task));

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Buscar"));
    }

    @Test
    void deveRetornarNotFoundQuandoTarefaNaoExiste() throws Exception {
        Mockito.when(taskService.getTaskById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/tasks/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveCriarTarefa() throws Exception {
        Task nova = new Task();
        nova.setTitle("Nova");

        Task salva = new Task();
        salva.setId(1L);
        salva.setTitle("Nova");

        Mockito.when(taskService.saveTask(any(Task.class))).thenReturn(salva);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nova)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Nova"));
    }

    @Test
    void deveAtualizarTarefa() throws Exception {
        Task atualizada = new Task();
        atualizada.setTitle("Atualizada");

        Task resposta = new Task();
        resposta.setId(1L);
        resposta.setTitle("Atualizada");

        Mockito.when(taskService.updateTask(eq(1L), any(Task.class))).thenReturn(resposta);

        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Atualizada"));
    }

    @Test
    void deveRetornarNotFoundAoAtualizarTarefaInexistente() throws Exception {
        Task atualizada = new Task();
        atualizada.setTitle("Qualquer");

        Mockito.when(taskService.updateTask(eq(999L), any(Task.class)))
                .thenThrow(new RuntimeException("Tarefa não encontrada"));

        mockMvc.perform(put("/api/tasks/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizada)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveDeletarTarefa() throws Exception {
        Mockito.doNothing().when(taskService).deleteTask(1L);

        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNoContent());
    }
}