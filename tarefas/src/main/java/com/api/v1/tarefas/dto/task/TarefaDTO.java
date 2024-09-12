package com.api.v1.tarefas.dto.task;

import com.api.v1.tarefas.dto.list.ListInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TarefaDTO {
    private UUID id;
    private String nomeTask;
    private String descricao;
    private String status;
    private boolean favoritos;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataUpdate;
}
