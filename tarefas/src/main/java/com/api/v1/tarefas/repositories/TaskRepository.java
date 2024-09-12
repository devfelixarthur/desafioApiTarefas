package com.api.v1.tarefas.repositories;

import com.api.v1.tarefas.entities.Lista;
import com.api.v1.tarefas.entities.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Tarefa, UUID> {


    boolean existsByNomeTaskAndLista(String nomeTask, Lista lista);
}
