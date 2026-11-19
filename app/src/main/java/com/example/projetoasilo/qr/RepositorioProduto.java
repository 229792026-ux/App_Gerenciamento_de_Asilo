package com.example.projetoasilo.qr;

import java.util.List;

public interface RepositorioProduto {

    /**
     * Cadastra um produto.
     *
     * @return true se cadastrou, false se já existe um produto com o mesmo código
     */
    boolean cadastrar(Produto produto);

    /** Lista todos os produtos cadastrados. */
    List<Produto> listar();

    /** Busca um produto pelo código. Retorna null se não encontrar. */
    Produto buscarPorCodigo(String codigo);

    /** Registra o uso de 1 unidade do produto (baixa de estoque). */
    ResultadoUso registrarUso(String codigo);
}
