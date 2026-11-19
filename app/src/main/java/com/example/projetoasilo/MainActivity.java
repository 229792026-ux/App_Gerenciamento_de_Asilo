package com.example.projetoasilo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projetoasilo.qr.Produto;
import com.example.projetoasilo.qr.QrProduto;
import com.example.projetoasilo.qr.QrScannerHelper;

public class MainActivity extends AppCompatActivity {

    private QrScannerHelper scannerHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imgQr = findViewById(R.id.imgQr);

        scannerHelper = new QrScannerHelper(this, conteudo ->
                Toast.makeText(this, conteudo, Toast.LENGTH_LONG).show());

        findViewById(R.id.btnGerar).setOnClickListener(v -> {
            try {
                Produto produto = new Produto("PROD-001", "Camiseta", 49.90);
                Bitmap bmp = QrProduto.gerar(produto, 600);
                imgQr.setImageBitmap(bmp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        findViewById(R.id.btnEscanear).setOnClickListener(v -> scannerHelper.escanear());
    }
}