package com.api.v1.tarefas.service;

import com.api.v1.tarefas.dto.list.CadastroListDTO;
import com.api.v1.tarefas.dto.list.EditarListDTO;
import com.api.v1.tarefas.entities.Lista;
import com.api.v1.tarefas.exceptions.ApiException;
import com.api.v1.tarefas.repositories.ListRepository;
import com.api.v1.tarefas.services.ListService;
import com.api.v1.tarefas.utils.ResponsePadraoDTO;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ListServiceTest {

    @Mock
    private ListRepository listRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private ListService listService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void cadastrarList_DeveCadastrarComSucesso() {
        CadastroListDTO form = new CadastroListDTO("Lista Teste", "Usuario Teste");

        when(listRepository.findByNomeLista(any())).thenReturn(Optional.empty());
        when(listRepository.save(any(Lista.class))).thenReturn(new Lista());

        ResponsePadraoDTO response = listService.cadastrarList(form);

        assertEquals("Lista cadastrada com sucesso.", response.getMensagem());
    }

    @Test
    void cadastrarList_DeveLancarExcecaoSeJaExiste() {
        CadastroListDTO form = new CadastroListDTO("Lista Teste", "Usuario Teste");

        when(listRepository.findByNomeLista(any())).thenReturn(Optional.of(new Lista()));

        ApiException exception = assertThrows(ApiException.class, () -> listService.cadastrarList(form));

        assertEquals("Já exite uma lista para o nome informado", exception.getMessage());
    }

    @Test
    void atualizarList_DeveAtualizarComSucesso() {
        EditarListDTO form = new EditarListDTO(UUID.randomUUID(), "Lista Atualizada", "Usuario Atualizado", "ATIVO");
        Lista listaExistente = new Lista();
        listaExistente.setId(form.getId());
        listaExistente.setNomeLista("Lista Antiga");

        when(listRepository.findById(form.getId())).thenReturn(Optional.of(listaExistente));
        when(listRepository.save(any(Lista.class))).thenReturn(listaExistente);

        ResponsePadraoDTO response = listService.atualizarList(form);

        assertEquals("Lista atualizada com sucesso.", response.getMensagem());
    }

    @Test
    void atualizarList_DeveLancarExcecaoSeNomeJaExiste() {
        EditarListDTO form = new EditarListDTO(UUID.randomUUID(), "Lista Atualizada", "Usuario Atualizado", "ATIVO");
        Lista listaExistente = new Lista();
        listaExistente.setId(form.getId());

        when(listRepository.findById(form.getId())).thenReturn(Optional.of(listaExistente));
        Lista listaComMesmoNome = new Lista();
        listaComMesmoNome.setId(UUID.randomUUID());
        when(listRepository.findByNomeLista(any())).thenReturn(Optional.of(listaComMesmoNome));

        ApiException exception = assertThrows(ApiException.class, () -> listService.atualizarList(form));

        assertEquals("Já existe uma lista com o nome informado.", exception.getMessage());
    }


    @Test
    void excluirList_DeveExcluirComSucesso() {
        Lista listaExistente = new Lista();
        listaExistente.setId(UUID.randomUUID());

        when(listRepository.findById(listaExistente.getId())).thenReturn(Optional.of(listaExistente));
        doNothing().when(listRepository).delete(any(Lista.class));

        ResponsePadraoDTO response = listService.excluirList(listaExistente.getId());

        assertEquals("Lista excluída com sucesso.", response.getMensagem());
    }

    @Test
    void excluirList_DeveLancarErroSeListaNaoExistir() {
        UUID idLista = UUID.randomUUID();
        when(listRepository.findById(idLista)).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () -> listService.excluirList(idLista));

        assertEquals("Lista não encontrada para o id informado.", exception.getMessage());
        verify(listRepository, never()).delete(any(Lista.class));
    }

    @Test
    void cadastrarList_DeveLancarErroSeFalhaNoBanco() {
        CadastroListDTO form = new CadastroListDTO("Lista Teste", "Usuario Teste");

        when(listRepository.findByNomeLista(any())).thenReturn(Optional.empty());
        when(listRepository.save(any(Lista.class))).thenThrow(new RuntimeException("Erro no banco"));

        ApiException exception = assertThrows(ApiException.class, () -> listService.cadastrarList(form));

        assertEquals("Falha ao salvar a lista", exception.getMessage());
    }

    @Test
    void atualizarList_DeveAtualizarSomenteNomeQuandoForPassado() {
        EditarListDTO form = new EditarListDTO(UUID.randomUUID(), "Lista Atualizada", null, null);
        Lista listaExistente = new Lista();
        listaExistente.setId(form.getId());
        listaExistente.setNomeLista("Lista Antiga");

        when(listRepository.findById(form.getId())).thenReturn(Optional.of(listaExistente));

        ResponsePadraoDTO response = listService.atualizarList(form);

        assertEquals("Lista atualizada com sucesso.", response.getMensagem());
        assertEquals("Lista Atualizada", listaExistente.getNomeLista());
        assertNull(listaExistente.getNomeUsuario());
    }

    @Test
    void atualizarList_DeveLancarErroSeParametrosInvalidos() {
        EditarListDTO form = new EditarListDTO(UUID.randomUUID(), "", "Novo Usuario", "INVALIDO");

        Lista listaExistente = new Lista();
        listaExistente.setId(form.getId());

        when(listRepository.findById(form.getId())).thenReturn(Optional.of(listaExistente));

        ApiException exception = assertThrows(ApiException.class, () -> listService.atualizarList(form));

        assertEquals("Status inválido. O status deve ser ATIVO ou INATIVO.", exception.getMessage());
    }
}
