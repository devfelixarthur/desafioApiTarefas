package com.api.v1.tarefas.dto.list;

import com.api.v1.tarefas.dto.task.TarefaDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListaComTarefasDTO {
    private UUID id;
    private String nomeLista;
    private String nomeUsuario;
    private String status;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataUpdate;
    private List<TarefaDTO> tarefas;
}
