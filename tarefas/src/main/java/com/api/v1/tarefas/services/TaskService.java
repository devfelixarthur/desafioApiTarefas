package com.api.v1.tarefas.services;

import com.api.v1.tarefas.dto.list.ListInfoDTO;
import com.api.v1.tarefas.dto.task.CadastroTaskDTO;
import com.api.v1.tarefas.dto.task.EditarTaskDTO;
import com.api.v1.tarefas.dto.task.ListTaskDTO;
import com.api.v1.tarefas.dto.task.TarefaComListaDTO;
import com.api.v1.tarefas.entities.Lista;
import com.api.v1.tarefas.entities.Tarefa;
import com.api.v1.tarefas.enums.Status;
import com.api.v1.tarefas.exceptions.ApiException;
import com.api.v1.tarefas.repositories.ListRepository;
import com.api.v1.tarefas.repositories.TaskRepository;
import com.api.v1.tarefas.utils.ResponsePadraoDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    ListRepository listRepository;

    @Autowired
    EntityManager entityManager;

    @Transactional
    public ResponsePadraoDTO cadastrarTask(@Valid CadastroTaskDTO form) {
        Tarefa task = new Tarefa();

        Lista lista = listRepository.findById(form.idLista())
                .orElseThrow(() -> new ApiException("Lista não encontrada para o ID informado.", HttpStatus.NOT_FOUND));

        boolean tarefaExistente = taskRepository.existsByNomeTaskAndLista(form.nomeTask(), lista);

        if (tarefaExistente) {
            throw new ApiException("Já existe uma tarefa com o nome '" + form.nomeTask() + "' na lista informada.", HttpStatus.BAD_REQUEST);
        }

        boolean isFavorito = form.favorito() != null && form.favorito().equals(1);

        task.setLista(lista);
        task.setNomeTask(form.nomeTask());
        task.setDescricao(form.descricao());
        task.setFavoritos(isFavorito);

        task.setDataCriacao(LocalDateTime.now());
        task.setDataUpdate(LocalDateTime.now());
        task.setStatus(Status.ATIVO);

        try {
            taskRepository.save(task);
            return ResponsePadraoDTO.sucesso("Tarefa cadastrada com sucesso.");
        } catch (Exception ex) {
            throw new ApiException("Erro ao cadastrar a tarefa.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ListTaskDTO listarTodas() {
        List<Tarefa> tarefas = taskRepository.findAll();

        if (tarefas.isEmpty()){
            throw new ApiException("Tarefas não encontradas", HttpStatus.NOT_FOUND);
        }

        List<TarefaComListaDTO> tarefasDTO = tarefas.stream()
                .sorted((t1, t2) -> Boolean.compare(t2.isFavoritos(), t1.isFavoritos()))
                .map(tarefa -> new TarefaComListaDTO(
                        tarefa.getId(),
                        tarefa.getNomeTask(),
                        tarefa.getDescricao(),
                        tarefa.getStatus().name(),
                        tarefa.isFavoritos(),
                        tarefa.getDataCriacao(),
                        tarefa.getDataUpdate(),
                        List.of(new ListInfoDTO(
                                tarefa.getLista().getId(),
                                tarefa.getLista().getNomeLista(),
                                tarefa.getLista().getNomeUsuario(),
                                tarefa.getLista().getStatus().name(),
                                tarefa.getLista().getDataCriacao(),
                                tarefa.getLista().getDataUpdate()
                        ))
                )).collect(Collectors.toList());

        int totalTarefas = tarefasDTO.size();
        return new ListTaskDTO(totalTarefas, tarefasDTO);
    }

    public ListTaskDTO listarTasksByFields(UUID idTask, String nomeTask, String status, Boolean isFavoritos) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tarefa> query = cb.createQuery(Tarefa.class);
        Root<Tarefa> tarefa = query.from(Tarefa.class);

        List<Predicate> predicates = new ArrayList<>();

        if (idTask != null) {
            predicates.add(cb.equal(tarefa.get("id"), idTask));
        }

        if (nomeTask != null && !nomeTask.isEmpty()) {
            predicates.add(cb.like(cb.lower(tarefa.get("nomeTask")), "%" + nomeTask.toLowerCase() + "%"));
        }

        if (status != null && !status.isEmpty()) {
            try {
                Status taskStatus = Status.valueOf(status.toUpperCase());
                predicates.add(cb.equal(tarefa.get("status"), taskStatus));
            } catch (IllegalArgumentException e) {
                throw new ApiException("Status inválido. Deve ser ATIVO ou INATIVO.", HttpStatus.BAD_REQUEST);
            }
        }

        if (isFavoritos != null) {
            predicates.add(cb.equal(tarefa.get("favoritos"), isFavoritos));
        }

        query.select(tarefa).where(cb.and(predicates.toArray(new Predicate[0])));
        query.orderBy(cb.desc(tarefa.get("favoritos")));

        TypedQuery<Tarefa> typedQuery = entityManager.createQuery(query);
        List<Tarefa> resultList = typedQuery.getResultList();

        if (resultList.isEmpty()) {
            throw new ApiException("Dados não encontrados para os parâmtreos informados.", HttpStatus.NOT_FOUND);
        }

        List<TarefaComListaDTO> tarefasDTO = resultList.stream()
                .map(t -> new TarefaComListaDTO(
                        t.getId(),
                        t.getNomeTask(),
                        t.getDescricao(),
                        t.getStatus().name(),
                        t.isFavoritos(),
                        t.getDataCriacao(),
                        t.getDataUpdate(),
                        List.of(new ListInfoDTO(
                                t.getLista().getId(),
                                t.getLista().getNomeLista(),
                                t.getLista().getNomeUsuario(),
                                t.getLista().getStatus().name(),
                                t.getLista().getDataCriacao(),
                                t.getLista().getDataUpdate()
                        ))
                )).collect(Collectors.toList());

        return new ListTaskDTO(tarefasDTO.size(), tarefasDTO);
    }

    @Transactional
    public ResponsePadraoDTO atualizarTask(@Valid EditarTaskDTO form) {
        Tarefa task = taskRepository.findById(form.getId())
                .orElseThrow(() -> new ApiException("Tarefa não encontrada.", HttpStatus.NOT_FOUND));

        if (form.getIdLista() != null) {
            Lista lista = listRepository.findById(form.getIdLista())
                    .orElseThrow(() -> new ApiException("Lista não encontrada para o ID informado.", HttpStatus.NOT_FOUND));
            task.setLista(lista);
        }

        if (form.getNomeTask() != null && !form.getNomeTask().isEmpty()) {
            task.setNomeTask(form.getNomeTask());
        }

        if (form.getDescricao() != null && !form.getDescricao().isEmpty()) {
            task.setDescricao(form.getDescricao());
        }

        if (form.getFavorito() != null) {
            boolean isFavorito = form.getFavorito().equals(1);
            task.setFavoritos(isFavorito);
        }

        if (form.getStatus() != null && !form.getStatus().isEmpty()) {
            try {
                Status taskStatus = Status.valueOf(form.getStatus().toUpperCase());
                task.setStatus(taskStatus);
            } catch (IllegalArgumentException e) {
                throw new ApiException("Status inválido. Deve ser ATIVO ou INATIVO.", HttpStatus.BAD_REQUEST);
            }
        }

        task.setDataUpdate(LocalDateTime.now());

        try {
            taskRepository.save(task);
            return ResponsePadraoDTO.sucesso("Tarefa atualizada com sucesso.");
        } catch (Exception ex) {
            throw new ApiException("Erro ao atualizar a tarefa.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Transactional
    public ResponsePadraoDTO excluirTask(UUID id) {
        Tarefa task = taskRepository.findById(id)
                .orElseThrow(() -> new ApiException("Tarefa não encontrada.", HttpStatus.NOT_FOUND));

        try {
            taskRepository.delete(task);
            return ResponsePadraoDTO.sucesso("Tarefa excluída com sucesso.");
        } catch (Exception ex) {
            throw new ApiException("Erro ao excluir a tarefa.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponsePadraoDTO favoritarTask(UUID idTask, Integer favorito) {
        Tarefa task = taskRepository.findById(idTask)
                .orElseThrow(() -> new ApiException("Tarefa não encontrada.", HttpStatus.NOT_FOUND));

        boolean isFavorito = favorito != null && favorito.equals(1);

        if (isFavorito && task.isFavoritos()) {
            throw new ApiException("A Tarefa já encontra-se marcada como favorita.", HttpStatus.BAD_REQUEST);
        }

        if (!isFavorito && !task.isFavoritos()) {
            throw new ApiException("Tarefa já econtra-se desmarcada como favorita.", HttpStatus.BAD_REQUEST);
        }

        task.setFavoritos(isFavorito);
        task.setDataUpdate(LocalDateTime.now());

        try {
            taskRepository.save(task);
            return ResponsePadraoDTO.sucesso(isFavorito ? "Tarefa marcada como favorita." : "Tarefa desmarcada como favorita.");
        } catch (Exception ex) {
            throw new ApiException("Erro ao atualizar o status de favorito.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
