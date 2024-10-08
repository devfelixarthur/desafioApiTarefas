package com.api.v1.tarefas.dto.list;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditarListDTO {

    @NotNull(message = "O campo 'id' não pode estar nulo.")
    private UUID id;

    @Size(max = 255, message = "O campo 'nomeLista' deve ter no máximo 255 caracteres.")
    private String nomeLista;

    @Size(max = 255, message = "O campo 'nomeUsuario' deve ter no máximo 255 caracteres.")
    private String nomeUsuario;

    private String status;
}
