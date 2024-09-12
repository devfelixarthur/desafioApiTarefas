package com.api.v1.tarefas.controller;

import com.api.v1.tarefas.dto.task.CadastroTaskDTO;
import com.api.v1.tarefas.dto.task.EditarTaskDTO;
import com.api.v1.tarefas.dto.task.ListTaskDTO;
import com.api.v1.tarefas.dto.task.TarefaDTO;
import com.api.v1.tarefas.exceptions.ApiException;
import com.api.v1.tarefas.services.TaskService;
import com.api.v1.tarefas.utils.ResponsePadraoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/task")
@Tag(name = "Tarefas", description = "Operaçãoes relacionadas a rotinas de Tarefas.")
public class TaskController {

    @Autowired
    TaskService taskService;

    @Operation(summary = "Cadastrar Tarefa", description = "Endpoint responsável pelo cadastro de uma nova tarefa.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cadastro realizado com sucesso.", content = @Content(schema = @Schema(implementation = ResponsePadraoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ResponsePadraoDTO.class))),
            @ApiResponse(responseCode = "500", description = "Erro na solicitação.", content = @Content(schema = @Schema(implementation = ResponsePadraoDTO.class)))
    })
    @PostMapping("/cadastrarTask")
    public ResponseEntity<ResponsePadraoDTO> cadastrarTask(@RequestBody @Valid CadastroTaskDTO form) {
        ResponsePadraoDTO responsePadraoDTO = taskService.cadastrarTask(form);
        return ResponseEntity.ok().body(responsePadraoDTO);
    }

    @Operation(summary = "Listar todas as Tarefas", description = "Endpoint para listar todas as tarefas cadastradas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listagem realizada com sucesso.", content = @Content(schema = @Schema(implementation = ListTaskDTO.class))),
            @ApiResponse(responseCode = "404", description = "Nenhuma tarefa encontrada.", content = @Content(schema = @Schema(implementation = ResponsePadraoDTO.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.", content = @Content(schema = @Schema(implementation = ResponsePadraoDTO.class)))
    })
    @GetMapping("/listarTasks")
    public ResponseEntity<ListTaskDTO> listarTodas() {
        ListTaskDTO resultado = taskService.listarTodas();
        return ResponseEntity.ok(resultado);
    }

    @Operation(summary = "Listar todas as Tarefas By Fields", description = "Endpoint para listar todos os registros de tarefas filtrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listagem realizada com sucesso.", content = @Content(schema = @Schema(implementation = ListTaskDTO.class))),
            @ApiResponse(responseCode = "404", description = "Nenhuma tarefa encontrada.", content = @Content(schema = @Schema(implementation = ResponsePadraoDTO.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.", content = @Content(schema = @Schema(implementation = ResponsePadraoDTO.class)))
    })
    @GetMapping("/listarTasksByFields")
    public ResponseEntity<ListTaskDTO> listarByFields(
            @RequestParam(required = false) UUID idTask,
            @RequestParam(required = false) String nomeTask,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Boolean isFavoritos) {
        ListTaskDTO resultado = taskService.listarTasksByFields(idTask, nomeTask, status, isFavoritos);
        return ResponseEntity.ok(resultado);
    }

    @Operation(summary = "Atualizar Tarefa", description = "Endpoint para atualizar uma tarefa existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atualização realizada com sucesso.", content = @Content(schema = @Schema(implementation = ResponsePadraoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada.", content = @Content(schema = @Schema(implementation = ResponsePadraoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ResponsePadraoDTO.class)))
    })
    @PatchMapping("/atualizarTask")
    public ResponseEntity<ResponsePadraoDTO> atualizarTarefa(@RequestBody @Valid EditarTaskDTO form) {
        ResponsePadraoDTO responsePadraoDTO = taskService.atualizarTask(form);
        return ResponseEntity.ok().body(responsePadraoDTO);
    }

    @Operation(summary = "Excluir Tarefa", description = "Endpoint para excluir uma tarefa existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exclusão realizada com sucesso.", content = @Content(schema = @Schema(implementation = ResponsePadraoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada.", content = @Content(schema = @Schema(implementation = ResponsePadraoDTO.class)))
    })
    @DeleteMapping("/excluirTask/{id}")
    public ResponseEntity<ResponsePadraoDTO> excluirTarefa(@PathVariable UUID id) {
        ResponsePadraoDTO responsePadraoDTO = taskService.excluirTask(id);
        return ResponseEntity.ok().body(responsePadraoDTO);
    }

    @Operation(summary = "Favoritar/Desfavoritar Tarefa", description = "Endpoint para favoritar ou desfavoritar uma tarefa.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso.", content = @Content(schema = @Schema(implementation = ResponsePadraoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada.", content = @Content(schema = @Schema(implementation = ResponsePadraoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ResponsePadraoDTO.class)))
    })
    @PutMapping("/favoritarTask/{idTask}")
    public ResponseEntity<ResponsePadraoDTO> favoritarTarefa(
            @PathVariable(required = true) UUID idTask,
            @RequestParam(required = false) Integer favorito) {

        if (favorito == null || (favorito != 0 && favorito != 1)) {
            throw new ApiException("O parâmetro 'favorito' deve ser 0 para 'NÃO' ou 1 para 'SIM'.", HttpStatus.BAD_REQUEST);
        }
        ResponsePadraoDTO responsePadraoDTO = taskService.favoritarTask(idTask, favorito);
        return ResponseEntity.ok().body(responsePadraoDTO);
    }


}
