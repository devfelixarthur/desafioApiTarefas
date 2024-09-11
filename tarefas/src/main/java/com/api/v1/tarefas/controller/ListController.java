package com.api.v1.tarefas.controller;


import com.api.v1.tarefas.dto.CadastroListDTO;
import com.api.v1.tarefas.dto.ListaDTO;
import com.api.v1.tarefas.entities.Lista;
import com.api.v1.tarefas.services.ListService;
import com.api.v1.tarefas.utils.ResponsePadraoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/list")
@Tag(name = "Lista de Tarefas", description = "Operaçãoes relacionadas a operações com as Listas de Tarefas.")
public class ListController {

    @Autowired
    ListService listService;

    @Operation(summary = "Cadastrar Lista", description = "Endpoint responsável pelo cadastro de uma nova lista de tarefas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cadastro Realizado com Sucesso.", content = @Content(schema = @Schema(implementation = ResponsePadraoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados Inválidos", content = @Content(schema = @Schema(implementation = ResponsePadraoDTO.class))),
            @ApiResponse(responseCode = "500", description = "Erro na solicitação.", content = @Content(schema = @Schema(implementation = ResponsePadraoDTO.class)))
    })
    @PostMapping("/cadastrarLista")
    public ResponseEntity<ResponsePadraoDTO> cadastrarWine(@RequestBody @Valid CadastroListDTO form) {
        ResponsePadraoDTO responsePadraoDTO = listService.cadastrarList(form);
        return ResponseEntity.ok().body(responsePadraoDTO);
    }

    @Operation(summary = "Listar todas as Listas", description = "Endpoint para listar todas as listas cadastradas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listagem realizada com sucesso.", content = @Content(schema = @Schema(implementation = ListaDTO.class))),
            @ApiResponse(responseCode = "404", description = "Nenhuma lista encontrada.", content = @Content(schema = @Schema(implementation = ResponsePadraoDTO.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.", content = @Content(schema = @Schema(implementation = ResponsePadraoDTO.class)))
    })
    @GetMapping("/listarTodas")
    public ResponseEntity<ListaDTO> listarTodas() {
        ListaDTO listas = listService.listarTodas();
        return ResponseEntity.ok(listas);
    }
}
