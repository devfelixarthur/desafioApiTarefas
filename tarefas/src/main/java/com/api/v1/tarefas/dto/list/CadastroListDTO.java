package com.api.v1.tarefas.dto.list;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record CadastroListDTO(
        @NotBlank(message = "O campo 'nomeLista' não pode estar em branco.")
        @Size(min = 3, max = 255, message = "O campo 'nomeLista' deve ter entre 3 e 255 caracteres.")
        String nomeLista,

        @NotBlank(message = "O campo 'nomeUsuario' não pode estar em branco.")
        @Size(min = 3, max = 255, message = "O campo 'nomeUsuario' deve ter entre 3 e 255 caracteres.")
        String nomeUsuario
) {
}
