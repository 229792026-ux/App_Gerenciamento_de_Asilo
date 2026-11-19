package com.example.projetoasilo.qr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ProdutoRepositorioMemoriaTest {

    private RepositorioProduto repo;

    @Before
    public void setUp() {
        repo = new ProdutoRepositorioMemoria();
    }

    @Test
    public void deveCadastrarProduto() {
        assertTrue(repo.cadastrar(new Produto("P1", "Fralda", 10)));
        assertEquals(1, repo.listar().size());
    }

    @Test
    public void naoDeveCadastrarCodigoRepetido() {
        repo.cadastrar(new Produto("P1", "Fralda", 10));
        assertFalse(repo.cadastrar(new Produto("P1", "Luva", 5)));
        assertEquals(1, repo.listar().size());
    }

    @Test
    public void deveBuscarProdutoPorCodigo() {
        repo.cadastrar(new Produto("P1", "Fralda", 10));
        Produto p = repo.buscarPorCodigo("P1");
        assertNotNull(p);
        assertEquals("Fralda", p.getNome());
        assertEquals(10, p.getQuantidade());
    }

    @Test
    public void deveRetornarNullQuandoProdutoNaoExiste() {
        assertNull(repo.buscarPorCodigo("XYZ"));
    }

    @Test
    public void deveDiminuirUmaUnidadeAoRegistrarUso() {
        repo.cadastrar(new Produto("P1", "Fralda", 10));

        ResultadoUso r = repo.registrarUso("P1");

        assertEquals(ResultadoUso.Status.OK, r.getStatus());
        assertEquals(9, r.getProduto().getQuantidade());
        assertEquals(9, repo.buscarPorCodigo("P1").getQuantidade());
    }

    @Test
    public void deveAvisarEstoqueBaixoAoChegarNoMinimo() {
        // quantidade mínima padrão = 5
        repo.cadastrar(new Produto("P1", "Fralda", 6));

        ResultadoUso r = repo.registrarUso("P1");

        assertEquals(ResultadoUso.Status.ESTOQUE_BAIXO, r.getStatus());
        assertEquals(5, r.getProduto().getQuantidade());
    }

    @Test
    public void naoDeveRegistrarUsoSemEstoque() {
        repo.cadastrar(new Produto("P1", "Fralda", 0));

        ResultadoUso r = repo.registrarUso("P1");

        assertEquals(ResultadoUso.Status.SEM_ESTOQUE, r.getStatus());
        assertEquals(0, repo.buscarPorCodigo("P1").getQuantidade());
    }

    @Test
    public void naoDeveFicarNegativoAoUsarAteZerar() {
        repo.cadastrar(new Produto("P1", "Fralda", 1));

        ResultadoUso primeiro = repo.registrarUso("P1");
        ResultadoUso segundo = repo.registrarUso("P1");

        assertEquals(0, primeiro.getProduto().getQuantidade());
        assertEquals(ResultadoUso.Status.SEM_ESTOQUE, segundo.getStatus());
        assertEquals(0, repo.buscarPorCodigo("P1").getQuantidade());
    }

    @Test
    public void deveInformarProdutoNaoEncontradoNoUso() {
        ResultadoUso r = repo.registrarUso("XYZ");

        assertEquals(ResultadoUso.Status.NAO_ENCONTRADO, r.getStatus());
        assertNull(r.getProduto());
    }
}
