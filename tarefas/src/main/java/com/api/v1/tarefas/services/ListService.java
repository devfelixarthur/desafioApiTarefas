package com.api.v1.tarefas.services;

import com.api.v1.tarefas.dto.CadastroListDTO;
import com.api.v1.tarefas.dto.ListaDTO;
import com.api.v1.tarefas.entities.Lista;
import com.api.v1.tarefas.enums.Status;
import com.api.v1.tarefas.exceptions.ApiException;
import com.api.v1.tarefas.repositories.ListRepository;
import com.api.v1.tarefas.utils.ResponsePadraoDTO;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ListService {

    @Autowired
    ListRepository listRepository;



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
}
