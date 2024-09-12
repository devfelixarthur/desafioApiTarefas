package com.api.v1.tarefas.controller;

import com.api.v1.tarefas.dto.list.CadastroListDTO;
import com.api.v1.tarefas.dto.list.EditarListDTO;
import com.api.v1.tarefas.dto.list.ListaDTO;
import com.api.v1.tarefas.services.ListService;
import com.api.v1.tarefas.utils.ResponsePadraoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ListControllerTest {

    @Mock
    private ListService listService;

    @InjectMocks
    private ListController listController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(listController).build();
    }

    @Test
    void cadastrarList_DeveRetornarSucesso() {
        CadastroListDTO form = new CadastroListDTO("Lista Teste", "Usuario Teste");
        ResponsePadraoDTO responseEsperado = ResponsePadraoDTO.sucesso("Lista cadastrada com sucesso.");

        when(listService.cadastrarList(any(CadastroListDTO.class))).thenReturn(responseEsperado);

        ResponseEntity<ResponsePadraoDTO> response = listController.cadastrarLista(form);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(responseEsperado, response.getBody());
    }

    @Test
    void listarTodas_DeveRetornarListas() {
        ListaDTO listaDTO = new ListaDTO(1, null);

        when(listService.listarTodas()).thenReturn(listaDTO);

        ResponseEntity<ListaDTO> response = listController.listarTodas();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(listaDTO, response.getBody());
    }

    @Test
    void listarByFields_DeveRetornarListasFiltradas() {
        ListaDTO listaDTO = new ListaDTO(1, null);
        when(listService.listarByFields(any(), any(), any(), any())).thenReturn(listaDTO);

        ResponseEntity<ListaDTO> response = listController.listarByFields(UUID.randomUUID(), "usuario", "lista", "ativo");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(listaDTO, response.getBody());
    }

    @Test
    void atualizarLista_DeveRetornarSucesso() {
        EditarListDTO form = new EditarListDTO(UUID.randomUUID(), "Lista Teste", "Usuario Teste", "ATIVO");
        ResponsePadraoDTO responseEsperado = ResponsePadraoDTO.sucesso("Lista atualizada com sucesso.");

        when(listService.atualizarList(any(EditarListDTO.class))).thenReturn(responseEsperado);

        ResponseEntity<ResponsePadraoDTO> response = listController.atualizarLista(form);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(responseEsperado, response.getBody());
    }

    @Test
    void excluirLista_DeveRetornarSucesso() {
        ResponsePadraoDTO responseEsperado = ResponsePadraoDTO.sucesso("Lista excluída com sucesso.");

        when(listService.excluirList(any(UUID.class))).thenReturn(responseEsperado);

        ResponseEntity<ResponsePadraoDTO> response = listController.excluirLista(UUID.randomUUID());

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(responseEsperado, response.getBody());
    }

    @Test
    void listarByFields_DeveRetornarListaFiltrada_SomentePorId() {
        UUID id = UUID.randomUUID();
        ListaDTO listaDTO = new ListaDTO(1, null);

        when(listService.listarByFields(eq(id), isNull(), isNull(), isNull())).thenReturn(listaDTO);

        ResponseEntity<ListaDTO> response = listController.listarByFields(id, null, null, null);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(listaDTO, response.getBody());

        verify(listService, times(1)).listarByFields(eq(id), isNull(), isNull(), isNull());
    }

    @Test
    void listarByFields_DeveRetornarListaFiltrada_PorNomeUsuarioEStatus() {
        ListaDTO listaDTO = new ListaDTO(1, null);

        when(listService.listarByFields(isNull(), eq("Usuario Teste"), isNull(), eq("ATIVO"))).thenReturn(listaDTO);

        ResponseEntity<ListaDTO> response = listController.listarByFields(null, "Usuario Teste", null, "ATIVO");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(listaDTO, response.getBody());

        verify(listService, times(1)).listarByFields(isNull(), eq("Usuario Teste"), isNull(), eq("ATIVO"));
    }


}
