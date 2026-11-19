package com.example.projetoasilo.qr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.graphics.Bitmap;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class QrFluxoTest {

    private RepositorioProduto repo;

    @Before
    public void setUp() {
        repo = new ProdutoRepositorioMemoria();
    }

    /** Lê o texto de dentro de um QR, como a câmera faria. */
    private String lerQr(Bitmap bmp) throws Exception {
        int largura = bmp.getWidth();
        int altura = bmp.getHeight();
        int[] pixels = new int[largura * altura];
        bmp.getPixels(pixels, 0, largura, 0, 0, largura, altura);

        RGBLuminanceSource fonte = new RGBLuminanceSource(largura, altura, pixels);
        BinaryBitmap binario = new BinaryBitmap(new HybridBinarizer(fonte));
        return new QRCodeReader().decode(binario).getText();
    }

    @Test
    public void qrGeradoDeveConterOCodigoDoProduto() throws Exception {
        Produto produto = new Produto("PROD-001", "Fralda", 10);

        Bitmap bmp = QrProduto.gerar(produto, 600);

        assertNotNull(bmp);
        assertEquals("PROD-001", lerQr(bmp));
    }

    @Test
    public void qrDeveGuardarCodigoComCaracteresEspeciais() throws Exception {
        Produto produto = new Produto("LUVA-Ç/09", "Luva", 10);

        Bitmap bmp = QrProduto.gerar(produto, 600);

        assertEquals("LUVA-Ç/09", lerQr(bmp));
    }

    @Test
    public void fluxoCompletoCadastroQrLeituraEBaixa() throws Exception {
        // 1. Cadastra
        repo.cadastrar(new Produto("PROD-001", "Fralda", 7));

        // 2. Gera o QR do produto cadastrado
        Produto cadastrado = repo.buscarPorCodigo("PROD-001");
        Bitmap bmp = QrProduto.gerar(cadastrado, 600);

        // 3. "Escaneia": lê o código de dentro do QR
        String codigoLido = lerQr(bmp);
        assertEquals("PROD-001", codigoLido);

        // 4. Registra o uso com o código lido
        ResultadoUso r = repo.registrarUso(codigoLido);
        assertEquals(ResultadoUso.Status.OK, r.getStatus());
        assertEquals(6, r.getProduto().getQuantidade());

        // 5. Segundo uso chega ao mínimo (5) e avisa estoque baixo
        ResultadoUso r2 = repo.registrarUso(lerQr(bmp));
        assertEquals(ResultadoUso.Status.ESTOQUE_BAIXO, r2.getStatus());
        assertEquals(5, r2.getProduto().getQuantidade());
    }
}
