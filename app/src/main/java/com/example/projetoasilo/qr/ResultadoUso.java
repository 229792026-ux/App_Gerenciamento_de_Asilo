package com.example.projetoasilo.qr;

public class ResultadoUso {

    public enum Status {
        NAO_ENCONTRADO,   // código não cadastrado
        SEM_ESTOQUE,      // quantidade já está zerada
        OK,               // baixa feita, estoque normal
        ESTOQUE_BAIXO     // baixa feita, quantidade chegou ao mínimo ou abaixo
    }

    private final Status status;
    private final Produto produto;

    public ResultadoUso(Status status, Produto produto) {
        this.status = status;
        this.produto = produto;
    }

    public Status getStatus() {
        return status;
    }

    /** Produto já com a quantidade atualizada. É null quando NAO_ENCONTRADO. */
    public Produto getProduto() {
        return produto;
    }
}
