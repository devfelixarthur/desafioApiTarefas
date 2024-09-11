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

    @NotBlank
    @Column(name = "list_name", unique = true, nullable = false, length = 255)
    private String nomeLista;

    @NotBlank
    @Column(name = "created_by", nullable = false, length = 255)
    private String nomeUsuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "date_created", nullable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "date_update", nullable = false)
    private LocalDateTime dataUpdate;

    @OneToMany(mappedBy = "lista", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Tarefa> tarefas;
}
