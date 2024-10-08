package com.api.v1.tarefas.services;

import com.api.v1.tarefas.dto.list.CadastroListDTO;
import com.api.v1.tarefas.dto.list.EditarListDTO;
import com.api.v1.tarefas.dto.list.ListaComTarefasDTO;
import com.api.v1.tarefas.dto.list.ListaDTO;
import com.api.v1.tarefas.dto.task.TarefaDTO;
import com.api.v1.tarefas.entities.Lista;
import com.api.v1.tarefas.entities.Tarefa;
import com.api.v1.tarefas.enums.Status;
import com.api.v1.tarefas.exceptions.ApiException;
import com.api.v1.tarefas.repositories.ListRepository;
import com.api.v1.tarefas.utils.ResponsePadraoDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ListService {

    @Autowired
    ListRepository listRepository;

    @Autowired
    private EntityManager entityManager;



    @Transactional
    public ResponsePadraoDTO cadastrarList(@Valid CadastroListDTO form) {
        boolean listaExiste = listRepository.findByNomeLista(form.nomeLista()).isPresent();

        if (listaExiste) {
            throw new ApiException("Já exite uma lista para o nome informado", HttpStatus.BAD_REQUEST);
        }

        Lista novaLista = new Lista();
        novaLista.setNomeLista(form.nomeLista());
        novaLista.setNomeUsuario(form.nomeUsuario());
        novaLista.setDataCriacao(LocalDateTime.now());
        novaLista.setDataUpdate(LocalDateTime.now());
        novaLista.setStatus(Status.ATIVO);

        try {
            listRepository.save(novaLista);
            return ResponsePadraoDTO.sucesso("Lista cadastrada com sucesso.");
        } catch (Exception ex) {
            throw new ApiException("Falha ao salvar a lista", HttpStatus.BAD_REQUEST);
        }
    }

    public ListaDTO listarTodas() {
        ListaDTO result = new ListaDTO();
        List<Lista> listasCadastradas = listRepository.findAllListas();
        int total = listRepository.countTotal();

        List<ListaComTarefasDTO> listasDTO = listasCadastradas.stream().map(lista -> {
            List<TarefaDTO> tarefasDTO = lista.getTarefas().stream()
                    .sorted(Comparator.comparing(Tarefa::isFavoritos).reversed())
                    .map(tarefa -> new TarefaDTO(
                            tarefa.getId(),
                            tarefa.getNomeTask(),
                            tarefa.getDescricao(),
                            tarefa.getStatus().name(),
                            tarefa.isFavoritos(),
                            tarefa.getDataCriacao(),
                            tarefa.getDataUpdate()
                    )).collect(Collectors.toList());

            return new ListaComTarefasDTO(
                    lista.getId(),
                    lista.getNomeLista(),
                    lista.getNomeUsuario(),
                    lista.getStatus().name(),
                    lista.getDataCriacao(),
                    lista.getDataUpdate(),
                    tarefasDTO
            );
        }).collect(Collectors.toList());

        result.setListaTarefas(listasDTO);
        result.setTotalListas(total);

        if (listasCadastradas.isEmpty()) {
            throw new ApiException("Listas não encontradas.", HttpStatus.NOT_FOUND);
        }

        return result;
    }


    public ListaDTO listarByFields(UUID id, String nomeUsuario, String nomeLista, String status) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Lista> query = cb.createQuery(Lista.class);
        Root<Lista> lista = query.from(Lista.class);

        List<Predicate> predicates = new ArrayList<>();

        if (id != null) {
            predicates.add(cb.equal(lista.get("id"), id));
        }

        if (nomeUsuario != null && !nomeUsuario.isEmpty()) {
            predicates.add(cb.like(cb.lower(lista.get("nomeUsuario")), "%" + nomeUsuario.toLowerCase() + "%"));
        }

        if (nomeLista != null && !nomeLista.isEmpty()) {
            predicates.add(cb.like(cb.lower(lista.get("nomeLista")), "%" + nomeLista.toLowerCase() + "%"));
        }

        if (status != null && !status.isEmpty()) {
            String statusUpper = status.toUpperCase();
            try {
                Status statusEnum = Status.valueOf(statusUpper);
                predicates.add(cb.equal(lista.get("status"), statusEnum));
            } catch (IllegalArgumentException e) {
                throw new ApiException("Status inválido", "O status deve ser ATIVO ou INATIVO.", HttpStatus.BAD_REQUEST);
            }
        }

        query.select(lista).where(cb.and(predicates.toArray(new Predicate[0])));

        TypedQuery<Lista> typedQuery = entityManager.createQuery(query);
        List<Lista> resultList = typedQuery.getResultList();
        int total = resultList.size();

        if (resultList.isEmpty()) {
            throw new ApiException("Dados não encontrados para os parâmetros informados.", HttpStatus.NOT_FOUND);
        }

        List<ListaComTarefasDTO> listasDTO = resultList.stream().map(listaRetorno -> {
            List<TarefaDTO> tarefasDTO = listaRetorno.getTarefas().stream()
                    .sorted(Comparator.comparing(Tarefa::isFavoritos).reversed())
                    .map(tarefa -> new TarefaDTO(
                            tarefa.getId(),
                            tarefa.getNomeTask(),
                            tarefa.getDescricao(),
                            tarefa.getStatus().name(),
                            tarefa.isFavoritos(),
                            tarefa.getDataCriacao(),
                            tarefa.getDataUpdate()
                    )).collect(Collectors.toList());

            return new ListaComTarefasDTO(
                    listaRetorno.getId(),
                    listaRetorno.getNomeLista(),
                    listaRetorno.getNomeUsuario(),
                    listaRetorno.getStatus().name(),
                    listaRetorno.getDataCriacao(),
                    listaRetorno.getDataUpdate(),
                    tarefasDTO
            );
        }).collect(Collectors.toList());

        return new ListaDTO(total, listasDTO);
    }

    public ResponsePadraoDTO atualizarList(@Valid EditarListDTO form) {
        Lista listaExistente = listRepository.findById(form.getId())
                .orElseThrow(() -> new ApiException("Lista não encontrada para o id informado.", HttpStatus.NOT_FOUND));

        if (form.getNomeLista() != null && !form.getNomeLista().isEmpty()) {
            boolean nomeJaExiste = listRepository.findByNomeLista(form.getNomeLista())
                    .filter(lista -> !lista.getId().equals(form.getId()))
                    .isPresent();

            if (nomeJaExiste) {
                throw new ApiException("Já existe uma lista com o nome informado.", HttpStatus.BAD_REQUEST);
            }

            listaExistente.setNomeLista(form.getNomeLista());
        }

        if (form.getNomeUsuario() != null && !form.getNomeUsuario().isEmpty()) {
            listaExistente.setNomeUsuario(form.getNomeUsuario());
        }

        if (form.getStatus() != null) {
            String statusUpper = form.getStatus().toUpperCase();
            if (!statusUpper.equals(Status.ATIVO.name()) && !statusUpper.equals(Status.INATIVO.name())) {
                throw new ApiException("Status inválido. O status deve ser ATIVO ou INATIVO.", HttpStatus.BAD_REQUEST);
            }
            listaExistente.setStatus(Status.valueOf(statusUpper));
        }

        listaExistente.setDataUpdate(LocalDateTime.now());

        try {
            listRepository.save(listaExistente);
            return ResponsePadraoDTO.sucesso("Lista atualizada com sucesso.");
        } catch (Exception ex) {
            throw new ApiException("Erro ao atualizar a lista.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponsePadraoDTO excluirList(UUID id) {
        Lista listaExistente = listRepository.findById(id)
                .orElseThrow(() -> new ApiException("Lista não encontrada para o id informado.", HttpStatus.NOT_FOUND));

        try {
            listRepository.delete(listaExistente);
            return ResponsePadraoDTO.sucesso("Lista excluída com sucesso.");
        } catch (Exception ex) {
            throw new ApiException("Erro ao excluir a lista.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
