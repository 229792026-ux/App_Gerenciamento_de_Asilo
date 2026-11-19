package com.example.projetoasilo.qr;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class QrScannerHelper {

    public interface Listener {
        void aoLerQr(String conteudo);
    }

    private final ActivityResultLauncher<ScanOptions> launcher;

    /**
     * IMPORTANTE: criar esta classe no onCreate (ou como campo) da Activity,
     * nunca dentro de um clique de botão.
     */
    public QrScannerHelper(AppCompatActivity activity, Listener listener) {
        launcher = activity.registerForActivityResult(new ScanContract(), result -> {
            if (result.getContents() != null) {
                listener.aoLerQr(result.getContents());
            }
        });
    }

    public void escanear() {
        ScanOptions options = new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
        options.setPrompt("Aponte para o QR code");
        options.setBeepEnabled(false);
        launcher.launch(options);
    }
}
