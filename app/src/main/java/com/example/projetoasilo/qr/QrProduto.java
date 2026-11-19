package com.example.projetoasilo.qr;

import android.graphics.Bitmap;

public final class QrProduto {

    private QrProduto() {
        // Classe utilitária, não precisa ser instanciada
    }

    /**
     * Gera o QR code de um produto.
     * O conteúdo do QR é o código do produto.
     *
     * @param produto produto que vai virar QR code
     * @param tamanhoPx largura e altura da imagem em pixels
     * @return Bitmap com o QR code do produto
     */
    public static Bitmap gerar(Produto produto, int tamanhoPx) throws Exception {
        return QrGenerator.gerar(produto.getCodigo(), tamanhoPx);
    }
}