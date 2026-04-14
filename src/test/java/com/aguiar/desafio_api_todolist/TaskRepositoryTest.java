package com.aguiar.desafio_api_todolist.repository;

import com.aguiar.desafio_api_todolist.entity.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void deveSalvarETrazerTarefaPorTitulo() {

        Task task = new Task();
        task.setTitle("Estudar Spring");
        task.setDescription("Focar no módulo de segurança");

        taskRepository.save(task);

        List<Task> resultado = taskRepository.findByTitleContaining("Spring");

        assertThat(resultado).isNotEmpty();
        assertThat(resultado.get(0).getTitle()).contains("Spring");
    }
}