package com.example.projetoasilo.qr;

import java.util.ArrayList;
import java.util.List;

public class ProdutoRepositorioMemoria implements RepositorioProduto {

    private final List<Produto> produtos = new ArrayList<>();

    @Override
    public boolean cadastrar(Produto produto) {
        if (buscarPorCodigo(produto.getCodigo()) != null) {
            return false;
        }
        produtos.add(produto);
        return true;
    }

    @Override
    public List<Produto> listar() {
        return new ArrayList<>(produtos);
    }

    @Override
    public Produto buscarPorCodigo(String codigo) {
        for (Produto p : produtos) {
            if (p.getCodigo().equals(codigo)) {
                return p;
            }
        }
        return null;
    }

    @Override
    public ResultadoUso registrarUso(String codigo) {
        for (int i = 0; i < produtos.size(); i++) {
            Produto atual = produtos.get(i);
            if (!atual.getCodigo().equals(codigo)) {
                continue;
            }

            if (atual.getQuantidade() <= 0) {
                return new ResultadoUso(ResultadoUso.Status.SEM_ESTOQUE, atual);
            }

            Produto novo = atual.comQuantidade(atual.getQuantidade() - 1);
            produtos.set(i, novo);

            ResultadoUso.Status status =
                    novo.getQuantidade() <= novo.getQuantidadeMinima()
                            ? ResultadoUso.Status.ESTOQUE_BAIXO
                            : ResultadoUso.Status.OK;
            return new ResultadoUso(status, novo);
        }
        return new ResultadoUso(ResultadoUso.Status.NAO_ENCONTRADO, null);
    }
}