package com.api.v1.tarefas.dto.list;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListInfoDTO {
    private UUID idLista;
    private String nomeLista;
    private String nomeUsuario;
    private String statusLista;
    private LocalDateTime dataCriacaoLista;
    private LocalDateTime dataUpdateLista;
}
