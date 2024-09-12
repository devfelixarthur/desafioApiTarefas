package com.api.v1.tarefas.dto.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListTaskDTO {
    private int totalTarefas;
    private List<TarefaComListaDTO> tarefas;
}
