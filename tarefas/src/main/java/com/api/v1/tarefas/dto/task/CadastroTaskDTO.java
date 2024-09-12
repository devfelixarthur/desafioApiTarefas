    package com.api.v1.tarefas.dto.task;

    import jakarta.validation.constraints.Max;
    import jakarta.validation.constraints.Min;
    import jakarta.validation.constraints.NotBlank;
    import jakarta.validation.constraints.Size;

    import java.util.UUID;

    public record CadastroTaskDTO(
            @NotBlank(message = "O campo 'idList' não pode estar em branco. Exemplo: '123e4567-e89b-12d3-a456-556642440000'.")
            UUID idLista,

            @NotBlank(message = "O campo 'nomeTask' é obrigatório e deve conter entre 3 e 255 caracteres.")
            @Size(min = 3, max = 255, message = "O campo 'nomeTask' deve ter entre 3 e 255 caracteres.")
            String nomeTask,

            @Size(max = 500, message = "O campo 'descrição' deve ter no máximo 500 caracteres.")
            String descricao,

            @Min(value = 0, message = "O campo 'favorito' deve ser 0 para NÃO.")
            @Max(value = 1, message = "O campo 'favorito' deve ser 1 para SIM.")
            Integer favorito
    ) {
    }
