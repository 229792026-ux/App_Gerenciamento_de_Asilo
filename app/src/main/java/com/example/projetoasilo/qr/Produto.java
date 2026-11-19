package com.example.projetoasilo.qr;

public class Produto {

    // Valor provisório, até o time definir o mínimo de cada produto
    public static final int QUANTIDADE_MINIMA_PADRAO = 5;

    private final String codigo;
    private final String nome;
    private final int quantidade;
    private final int quantidadeMinima;

    public Produto(String codigo, String nome, int quantidade, int quantidadeMinima) {
        this.codigo = codigo;
        this.nome = nome;
        this.quantidade = quantidade;
        this.quantidadeMinima = quantidadeMinima;
    }

    public Produto(String codigo, String nome, int quantidade) {
        this(codigo, nome, quantidade, QUANTIDADE_MINIMA_PADRAO);
    }

    // Mantido para não quebrar o código que já existe
    public Produto(String codigo, String nome) {
        this(codigo, nome, 0, QUANTIDADE_MINIMA_PADRAO);
    }

    /** Devolve uma cópia do produto com outra quantidade. */
    public Produto comQuantidade(int novaQuantidade) {
        return new Produto(codigo, nome, novaQuantidade, quantidadeMinima);
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public int getQuantidadeMinima() {
        return quantidadeMinima;
    }
}