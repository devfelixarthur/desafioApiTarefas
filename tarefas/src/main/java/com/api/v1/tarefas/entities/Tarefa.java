package com.api.v1.tarefas.entities;

import com.api.v1.tarefas.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_tarefas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tarefa {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false, updatable = false)
    private UUID id;

    @NotBlank
    @Column(name = "nome_task", nullable = false, length = 255)
    private String nomeTask;

    @Column(name = "description", nullable = false, length = 500)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "is_favorite", nullable = false)
    private boolean favoritos;

    @Column(name = "date_created", nullable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "date_update", nullable = false)
    private LocalDateTime dataUpdate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_list", nullable = false)
    private Lista lista;
}
