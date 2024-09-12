package com.api.v1.tarefas.repositories;


import com.api.v1.tarefas.entities.Lista;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ListRepository extends JpaRepository<Lista, UUID> {

    Optional<Lista> findByNomeLista(String nomeLista);

    @Query("SELECT COUNT(l.id) FROM Lista l")
    int countTotal();

    @Query("SELECT l FROM Lista l JOIN FETCH l.tarefas t ORDER BY t.favoritos DESC")
    List<Lista> findAllListas();
}
