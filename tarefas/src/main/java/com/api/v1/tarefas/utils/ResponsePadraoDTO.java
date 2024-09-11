package com.api.v1.tarefas.utils;

import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponsePadraoDTO {
    private String titulo;
    private String mensagem;

    public ResponsePadraoDTO(String titulo, String mensagem) {
        this.titulo = titulo;
        this.mensagem = mensagem;
    }

    public String toJson(String callback) {
        JSONPObject jsonpObject = new JSONPObject(callback, this);
        return jsonpObject.toString();
    }

    public static ResponsePadraoDTO falha(String mensagem) {
        return new ResponsePadraoDTO("Falha.", mensagem);
    }

    public static ResponsePadraoDTO sucesso(String mensagem) {
        return new ResponsePadraoDTO("Sucesso", mensagem);
    }
}
