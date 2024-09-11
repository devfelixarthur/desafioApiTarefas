package com.api.v1.tarefas.entities;

import com.api.v1.tarefas.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tb_lista_tarefas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Lista {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false, updatable = false)
    private UUID id;

    @Column(name = "list_name", unique = true, nullable = false, length = 255)
    @NotBlank
    private String nomeLista;

    @Column(name = "created_by", nullable = false, length = 255)
    @NotBlank
    private String nomeUsuario;

    @Column(name = "date_created", nullable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "date_update", nullable = false)
    private LocalDateTime dataUpdate;

    private Status status;

    @ManyToMany
    @JoinTable(
            name = "lista_tarefa",
            joinColumns = @JoinColumn(name = "lista_id"),
            inverseJoinColumns = @JoinColumn(name = "tarefa_id")
    )
    private Set<Tarefa> tarefas;

}
