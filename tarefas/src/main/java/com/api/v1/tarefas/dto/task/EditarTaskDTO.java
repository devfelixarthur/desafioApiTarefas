package com.api.v1.tarefas.dto.task;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditarTaskDTO {

    @NotNull(message = "O campo 'id' não pode estar nulo.")
    private UUID id;

    UUID idLista;

    @Size(max = 255, message = "O campo 'nomeTask' deve ter no máximo 255 caracteres.")
    String nomeTask;

    @Size(max = 500, message = "O campo 'descrição' deve ter no máximo 500 caracteres.")
    String descricao;

    @Min(value = 0, message = "O campo 'favorito' deve ser 0 para NÃO.")
    @Max(value = 1, message = "O campo 'favorito' deve ser 1 para SIM.")
    Integer favorito;

    private String status;

}
