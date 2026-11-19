package com.example.projetoasilo.qr;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public final class QrGenerator {

    private QrGenerator() {
        // Classe utilitária, não precisa ser instanciada
    }

    /**
     * Gera um QR code a partir de um texto.
     *
     * @param conteudo texto ou link que vai dentro do QR
     * @param tamanhoPx largura e altura da imagem em pixels
     * @return Bitmap com o QR code
     */
    public static Bitmap gerar(String conteudo, int tamanhoPx) throws Exception {
        return new BarcodeEncoder()
                .encodeBitmap(conteudo, BarcodeFormat.QR_CODE, tamanhoPx, tamanhoPx);
    }
}
