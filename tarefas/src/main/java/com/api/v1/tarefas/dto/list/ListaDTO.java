package com.api.v1.tarefas.dto.list;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListaDTO {
    private int totalListas;
    private List<ListaComTarefasDTO> listaTarefas;
}

