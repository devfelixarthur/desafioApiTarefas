package com.api.v1.tarefas.dto;

import com.api.v1.tarefas.entities.Lista;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListaDTO {
    private int totalListas;
    private List<Lista> listaTarefas;
}
