package com.api.v1.tarefas.services;

import com.api.v1.tarefas.dto.list.CadastroListDTO;
import com.api.v1.tarefas.dto.list.EditarListDTO;
import com.api.v1.tarefas.entities.Lista;
import com.api.v1.tarefas.enums.Status;
import com.api.v1.tarefas.exceptions.ApiException;
import com.api.v1.tarefas.repositories.ListRepository;
import com.api.v1.tarefas.utils.ResponsePadraoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ListServiceTest {

    @InjectMocks
    private ListService listService;

    @Mock
    private ListRepository listRepository;

    private UUID uuid;
    private Lista lista;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        uuid = UUID.randomUUID();
        lista = new Lista();
        lista.setId(uuid);
        lista.setNomeLista("Lista Teste");
        lista.setNomeUsuario("Usuário Teste");
        lista.setDataCriacao(LocalDateTime.now());
        lista.setDataUpdate(LocalDateTime.now());
        lista.setStatus(Status.ATIVO);
    }

    @Test
    void cadastrarList_Sucesso() {
        CadastroListDTO cadastroListDTO = new CadastroListDTO("Lista Teste", "Usuário Teste");

        when(listRepository.findByNomeLista("Lista Teste")).thenReturn(Optional.empty());

        ResponsePadraoDTO response = listService.cadastrarList(cadastroListDTO);

        assertEquals("Sucesso", response.getTitulo());
        assertEquals("Lista cadastrada com sucesso.", response.getMensagem());
        verify(listRepository, times(1)).save(any(Lista.class));
    }

    @Test
    void cadastrarList_NomeDuplicado() {
        CadastroListDTO cadastroListDTO = new CadastroListDTO("Lista Teste", "Usuário Teste");

        when(listRepository.findByNomeLista("Lista Teste")).thenReturn(Optional.of(lista));

        ApiException exception = assertThrows(ApiException.class, () -> {
            listService.cadastrarList(cadastroListDTO);
        });

        assertEquals("Já exite uma lista para o nome informado", exception.getMessage());
        assertEquals(400, exception.getHttpStatus().value());
        verify(listRepository, never()).save(any(Lista.class));
    }

    @Test
    void atualizarList_Sucesso() {
        EditarListDTO editarListDTO = new EditarListDTO(uuid, "Lista Atualizada", "Usuário Atualizado", "ATIVO");

        when(listRepository.findById(uuid)).thenReturn(Optional.of(lista));
        when(listRepository.findByNomeLista("Lista Atualizada")).thenReturn(Optional.empty());

        ResponsePadraoDTO response = listService.atualizarList(editarListDTO);

        assertEquals("Sucesso", response.getTitulo());
        assertEquals("Lista atualizada com sucesso.", response.getMensagem());
        verify(listRepository, times(1)).save(lista);
    }

    @Test
    void atualizarList_NomeDuplicado() {
        EditarListDTO editarListDTO = new EditarListDTO(uuid, "Lista Atualizada", "Usuário Atualizado", "ATIVO");

        when(listRepository.findById(uuid)).thenReturn(Optional.of(lista));
        when(listRepository.findByNomeLista("Lista Atualizada")).thenReturn(Optional.of(new Lista()));

        ApiException exception = assertThrows(ApiException.class, () -> {
            listService.atualizarList(editarListDTO);
        });

        assertEquals("Já existe uma lista com o nome informado.", exception.getMessage());
        assertEquals(400, exception.getHttpStatus().value());
        verify(listRepository, never()).save(any(Lista.class));
    }

    @Test
    void atualizarList_StatusInvalido() {
        EditarListDTO editarListDTO = new EditarListDTO(uuid, "Lista Atualizada", "Usuário Atualizado", "INVALIDO");

        when(listRepository.findById(uuid)).thenReturn(Optional.of(lista));

        ApiException exception = assertThrows(ApiException.class, () -> {
            listService.atualizarList(editarListDTO);
        });

        assertEquals("Status inválido. O status deve ser ATIVO ou INATIVO.", exception.getMessage());
        assertEquals(400, exception.getHttpStatus().value());
        verify(listRepository, never()).save(any(Lista.class));
    }

    @Test
    void excluirList_Sucesso() {
        when(listRepository.findById(uuid)).thenReturn(Optional.of(lista));

        ResponsePadraoDTO response = listService.excluirList(uuid);

        assertEquals("Sucesso", response.getTitulo());
        assertEquals("Lista excluída com sucesso.", response.getMensagem());
        verify(listRepository, times(1)).delete(lista);
    }

    @Test
    void excluirList_ListaNaoEncontrada() {
        when(listRepository.findById(uuid)).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () -> {
            listService.excluirList(uuid);
        });

        assertEquals("Lista não encontrada para o UUID informado.", exception.getMessage());
        assertEquals(404, exception.getHttpStatus().value());
        verify(listRepository, never()).delete(any(Lista.class));
    }
}
