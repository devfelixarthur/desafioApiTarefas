package com.api.v1.tarefas.controller;

import com.api.v1.tarefas.dto.CadastroListDTO;
import com.api.v1.tarefas.exceptions.ApiException;
import com.api.v1.tarefas.services.ListService;
import com.api.v1.tarefas.utils.ResponsePadraoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ListControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private ListController listController;

    @Mock
    private ListService listService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(listController).build();
    }

    @Test
    void cadastrarLista_Sucesso() throws Exception {
        CadastroListDTO cadastroListDTO = new CadastroListDTO("Lista Teste", "Usuário Teste");

        when(listService.cadastrarList(any(CadastroListDTO.class)))
                .thenReturn(ResponsePadraoDTO.sucesso("Lista cadastrada com sucesso."));

        mockMvc.perform(post("/list/cadastrarLista")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nomeLista\":\"Lista Teste\",\"nomeUsuario\":\"Usuário Teste\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Sucesso"))
                .andExpect(jsonPath("$.mensagem").value("Lista cadastrada com sucesso."));
    }

    @Test
    void excluirLista_Sucesso() throws Exception {
        UUID uuid = UUID.randomUUID();

        when(listService.excluirList(uuid))
                .thenReturn(ResponsePadraoDTO.sucesso("Lista excluída com sucesso."));

        mockMvc.perform(delete("/list/excluirLista/{id}", uuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Sucesso"))
                .andExpect(jsonPath("$.mensagem").value("Lista excluída com sucesso."));
    }

    @Test
    void excluirLista_ListaNaoEncontrada() throws Exception {
        UUID uuid = UUID.randomUUID();

        when(listService.excluirList(uuid))
                .thenThrow(new ApiException("Lista não encontrada para o UUID informado.", HttpStatus.NOT_FOUND));

        mockMvc.perform(delete("/list/excluirLista/{id}", uuid))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.titulo").value("Falha."))
                .andExpect(jsonPath("$.mensagem").value("Lista não encontrada para o UUID informado."));
    }
}
