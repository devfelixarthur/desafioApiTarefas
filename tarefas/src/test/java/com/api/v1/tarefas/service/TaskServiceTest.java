package com.api.v1.tarefas.service;

import com.api.v1.tarefas.dto.task.CadastroTaskDTO;
import com.api.v1.tarefas.dto.task.EditarTaskDTO;
import com.api.v1.tarefas.dto.task.ListTaskDTO;
import com.api.v1.tarefas.entities.Lista;
import com.api.v1.tarefas.entities.Tarefa;
import com.api.v1.tarefas.enums.Status;
import com.api.v1.tarefas.exceptions.ApiException;
import com.api.v1.tarefas.repositories.ListRepository;
import com.api.v1.tarefas.repositories.TaskRepository;
import com.api.v1.tarefas.services.TaskService;
import com.api.v1.tarefas.utils.ResponsePadraoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ListRepository listRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void cadastrarTask_DeveCadastrarComSucesso() {
        CadastroTaskDTO form = new CadastroTaskDTO(UUID.randomUUID(), "Tarefa Teste", "Descrição Teste", 1);
        Lista lista = new Lista();
        when(listRepository.findById(any(UUID.class))).thenReturn(Optional.of(lista));
        when(taskRepository.existsByNomeTaskAndLista(anyString(), any(Lista.class))).thenReturn(false);

        ResponsePadraoDTO response = taskService.cadastrarTask(form);

        assertEquals("Tarefa cadastrada com sucesso.", response.getMensagem());
        verify(taskRepository, times(1)).save(any(Tarefa.class));
    }

    @Test
    void listarTodas_DeveRetornarErroSeNaoExistiremTarefas() {
        when(taskRepository.findAll()).thenReturn(java.util.Collections.emptyList());

        ApiException exception = assertThrows(ApiException.class, () -> {
            taskService.listarTodas();
        });

        assertEquals("Tarefas não encontradas", exception.getMessage());
    }

    @Test
    void excluirTask_DeveExcluirComSucesso() {
        Tarefa tarefa = new Tarefa();
        when(taskRepository.findById(any(UUID.class))).thenReturn(Optional.of(tarefa));

        ResponsePadraoDTO response = taskService.excluirTask(UUID.randomUUID());

        assertEquals("Tarefa excluída com sucesso.", response.getMensagem());
        verify(taskRepository, times(1)).delete(any(Tarefa.class));
    }

    @Test
    void favoritarTask_DeveMarcarComoFavorita() {
        Tarefa tarefa = new Tarefa();
        tarefa.setFavoritos(false);
        when(taskRepository.findById(any(UUID.class))).thenReturn(Optional.of(tarefa));

        ResponsePadraoDTO response = taskService.favoritarTask(UUID.randomUUID(), 1);

        assertEquals("Tarefa marcada como favorita.", response.getMensagem());
        verify(taskRepository, times(1)).save(any(Tarefa.class));
    }

    @Test
    void atualizarTask_DeveAtualizarComSucesso() {
        EditarTaskDTO form = new EditarTaskDTO(UUID.randomUUID(), UUID.randomUUID(), "Tarefa Teste", "Descrição Teste", 1, "ATIVO");
        Tarefa tarefa = new Tarefa();
        Lista lista = new Lista();

        when(taskRepository.findById(any(UUID.class))).thenReturn(Optional.of(tarefa));
        when(listRepository.findById(any(UUID.class))).thenReturn(Optional.of(lista));

        ResponsePadraoDTO response = taskService.atualizarTask(form);

        assertEquals("Tarefa atualizada com sucesso.", response.getMensagem());
        verify(taskRepository, times(1)).save(any(Tarefa.class));
    }

    @Test
    void cadastrarTask_DeveLancarErroSeNomeDuplicadoNaMesmaLista() {
        CadastroTaskDTO form = new CadastroTaskDTO(UUID.randomUUID(), "Tarefa Duplicada", "Descrição", 1);
        Lista lista = new Lista();

        when(listRepository.findById(any(UUID.class))).thenReturn(Optional.of(lista));
        when(taskRepository.existsByNomeTaskAndLista(anyString(), any(Lista.class))).thenReturn(true);

        ApiException exception = assertThrows(ApiException.class, () -> {
            taskService.cadastrarTask(form);
        });

        assertEquals("Já existe uma tarefa com o nome 'Tarefa Duplicada' na lista informada.", exception.getMessage());
    }


    @Test
    void favoritarTask_DeveLancarErroSeJaEstaFavoritada() {
        Tarefa tarefa = new Tarefa();
        tarefa.setFavoritos(true); // Já está marcada como favorita

        when(taskRepository.findById(any(UUID.class))).thenReturn(Optional.of(tarefa));

        ApiException exception = assertThrows(ApiException.class, () -> {
            taskService.favoritarTask(UUID.randomUUID(), 1); // Tentando favoritar novamente
        });

        assertEquals("A Tarefa já encontra-se marcada como favorita.", exception.getMessage());
    }


    @Test
    void excluirTask_DeveLancarErroSeTaskNaoEncontrada() {
        when(taskRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () -> {
            taskService.excluirTask(UUID.randomUUID());
        });

        assertEquals("Tarefa não encontrada.", exception.getMessage());
    }



}
