package com.api.v1.tarefas.controller;

import com.api.v1.tarefas.dto.task.CadastroTaskDTO;
import com.api.v1.tarefas.dto.task.EditarTaskDTO;
import com.api.v1.tarefas.dto.task.ListTaskDTO;
import com.api.v1.tarefas.exceptions.ApiException;
import com.api.v1.tarefas.services.TaskService;
import com.api.v1.tarefas.utils.ResponsePadraoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void cadastrarTask_DeveRetornarSucesso() {
        CadastroTaskDTO form = new CadastroTaskDTO(UUID.randomUUID(), "Tarefa Teste", "Descrição Teste", 1);
        ResponsePadraoDTO responseEsperado = ResponsePadraoDTO.sucesso("Tarefa cadastrada com sucesso.");

        when(taskService.cadastrarTask(any(CadastroTaskDTO.class))).thenReturn(responseEsperado);

        ResponseEntity<ResponsePadraoDTO> response = taskController.cadastrarTask(form);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseEsperado, response.getBody());
    }

    @Test
    void listarTodas_DeveRetornarTarefas() {
        ListTaskDTO listTaskDTO = new ListTaskDTO(1, null);
        when(taskService.listarTodas()).thenReturn(listTaskDTO);

        ResponseEntity<ListTaskDTO> response = taskController.listarTodas();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(listTaskDTO, response.getBody());
    }

    @Test
    void listarByFields_DeveRetornarTarefasFiltradas() {
        ListTaskDTO listTaskDTO = new ListTaskDTO(1, null);
        when(taskService.listarTasksByFields(any(), any(), any(), any())).thenReturn(listTaskDTO);

        ResponseEntity<ListTaskDTO> response = taskController.listarByFields(UUID.randomUUID(), "Tarefa Teste", "ATIVO", 1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(listTaskDTO, response.getBody());
    }

    @Test
    void atualizarTask_DeveRetornarSucesso() {
        EditarTaskDTO form = new EditarTaskDTO(UUID.randomUUID(), UUID.randomUUID(), "Tarefa Teste", "Descrição Teste", 1, "ATIVO");
        ResponsePadraoDTO responseEsperado = ResponsePadraoDTO.sucesso("Tarefa atualizada com sucesso.");

        when(taskService.atualizarTask(any(EditarTaskDTO.class))).thenReturn(responseEsperado);

        ResponseEntity<ResponsePadraoDTO> response = taskController.atualizarTarefa(form);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseEsperado, response.getBody());
    }

    @Test
    void excluirTask_DeveRetornarSucesso() {
        ResponsePadraoDTO responseEsperado = ResponsePadraoDTO.sucesso("Tarefa excluída com sucesso.");

        when(taskService.excluirTask(any(UUID.class))).thenReturn(responseEsperado);

        ResponseEntity<ResponsePadraoDTO> response = taskController.excluirTarefa(UUID.randomUUID());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseEsperado, response.getBody());
    }

    @Test
    void favoritarTask_DeveRetornarSucesso() {
        ResponsePadraoDTO responseEsperado = ResponsePadraoDTO.sucesso("Tarefa marcada como favorita.");

        when(taskService.favoritarTask(any(UUID.class), anyInt())).thenReturn(responseEsperado);

        ResponseEntity<ResponsePadraoDTO> response = taskController.favoritarTarefa(UUID.randomUUID(), 1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseEsperado, response.getBody());
    }

    @Test
    void listarByFields_DeveLancarErroSeIsFavoritosInvalido() {
        ApiException exception = assertThrows(ApiException.class, () -> {
            taskController.listarByFields(UUID.randomUUID(), "Tarefa Teste", "ATIVO", 2); // isFavoritos inválido
        });

        assertEquals("O parâmetro 'isFavoritos' deve ser 0 para 'NÃO' ou 1 para 'SIM'.", exception.getMessage());
    }


    @Test
    void excluirTask_DeveLancarErroSeTaskNaoEncontrada() {
        when(taskService.excluirTask(any(UUID.class))).thenThrow(new ApiException("Tarefa não encontrada.", HttpStatus.NOT_FOUND));

        ApiException exception = assertThrows(ApiException.class, () -> {
            taskController.excluirTarefa(UUID.randomUUID());
        });

        assertEquals("Tarefa não encontrada.", exception.getMessage());
    }

}
