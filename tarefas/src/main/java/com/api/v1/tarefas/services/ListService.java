package com.api.v1.tarefas.services;

import com.api.v1.tarefas.dto.CadastroListDTO;
import com.api.v1.tarefas.dto.EditarListDTO;
import com.api.v1.tarefas.dto.ListaDTO;
import com.api.v1.tarefas.entities.Lista;
import com.api.v1.tarefas.enums.Status;
import com.api.v1.tarefas.exceptions.ApiException;
import com.api.v1.tarefas.repositories.ListRepository;
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
        List<Lista> listasCadastradas = listRepository.findAll();
        int total = listRepository.countTotal();

        result.setListaTarefas(listasCadastradas);
        result.setTotalListas(total);
        if (listasCadastradas.isEmpty()){
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

        query.select(lista)
                .where(cb.and(predicates.toArray(new Predicate[0])));

        TypedQuery<Lista> typedQuery = entityManager.createQuery(query);

        List<Lista> resultList = typedQuery.getResultList();
        int total = resultList.size();

        if (resultList.isEmpty()){
            throw new ApiException("Dados não encontrados para os parâmteros informados.", HttpStatus.NOT_FOUND);
        }

        ListaDTO result = new ListaDTO(total, resultList);

        return result;
    }
    public ResponsePadraoDTO atualizarList(@Valid EditarListDTO form) {
        Lista listaExistente = listRepository.findById(form.getUuid())
                .orElseThrow(() -> new ApiException("Lista não encontrada para o UUID informado.", HttpStatus.NOT_FOUND));

        if (form.getNomeLista() != null && !form.getNomeLista().isEmpty()) {
            boolean nomeJaExiste = listRepository.findByNomeLista(form.getNomeLista())
                    .filter(lista -> !lista.getId().equals(form.getUuid()))
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
            String statusUpper = form.getStatus().toUpperCase(); // Converte para maiúsculo
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
                .orElseThrow(() -> new ApiException("Lista não encontrada para o UUID informado.", HttpStatus.NOT_FOUND));

        try {
            listRepository.delete(listaExistente);
            return ResponsePadraoDTO.sucesso("Lista excluída com sucesso.");
        } catch (Exception ex) {
            throw new ApiException("Erro ao excluir a lista.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
