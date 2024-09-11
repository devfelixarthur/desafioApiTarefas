package com.api.v1.tarefas.repositories;

import com.api.v1.tarefas.entities.Lista;
import com.api.v1.tarefas.enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ListRepositoryTest {

    @Autowired
    private ListRepository listRepository;

    private Lista lista;

    @BeforeEach
    void setUp() {
        lista = new Lista();
        lista.setId(UUID.randomUUID());
        lista.setNomeLista("Lista Teste");
        lista.setNomeUsuario("Usuário Teste");
        lista.setStatus(Status.ATIVO);
        lista.setDataCriacao(LocalDateTime.now());
        lista.setDataUpdate(LocalDateTime.now());
    }

    @Test
    void salvarLista_Sucesso() {
        Lista listaSalva = listRepository.save(lista);
        assertNotNull(listaSalva);
        assertEquals("Lista Teste", listaSalva.getNomeLista());
    }

    @Test
    void buscarPorNomeLista_Sucesso() {
        listRepository.save(lista);
        Optional<Lista> listaEncontrada = listRepository.findByNomeLista("Lista Teste");
        assertTrue(listaEncontrada.isPresent());
        assertEquals("Lista Teste", listaEncontrada.get().getNomeLista());
    }

    @Test
    void buscarPorNomeLista_NaoEncontrada() {
        Optional<Lista> listaEncontrada = listRepository.findByNomeLista("Lista Inexistente");
        assertFalse(listaEncontrada.isPresent());
    }

    @Test
    void excluirLista_Sucesso() {
        Lista listaSalva = listRepository.save(lista);
        listRepository.deleteById(listaSalva.getId());

        Optional<Lista> listaEncontrada = listRepository.findById(listaSalva.getId());
        assertFalse(listaEncontrada.isPresent());
    }
}
