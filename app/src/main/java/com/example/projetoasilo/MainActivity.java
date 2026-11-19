package com.example.projetoasilo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projetoasilo.qr.Produto;
import com.example.projetoasilo.qr.ProdutoRepositorioMemoria;
import com.example.projetoasilo.qr.QrProduto;
import com.example.projetoasilo.qr.QrScannerHelper;
import com.example.projetoasilo.qr.RepositorioProduto;
import com.example.projetoasilo.qr.ResultadoUso;

public class MainActivity extends AppCompatActivity {

    // static: a lista não se perde ao girar a tela
    private static final RepositorioProduto repositorio = new ProdutoRepositorioMemoria();

    private QrScannerHelper scannerHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText edtCodigo = findViewById(R.id.edtCodigo);
        EditText edtNome = findViewById(R.id.edtNome);
        EditText edtQuantidade = findViewById(R.id.edtQuantidade);
        ImageView imgQr = findViewById(R.id.imgQr);
        TextView txtResultado = findViewById(R.id.txtResultado);

        // Ao escanear: dá baixa de 1 unidade e mostra o resultado na tela
        scannerHelper = new QrScannerHelper(this, new QrScannerHelper.Listener() {
            @Override
            public void aoLerQr(String conteudo) {
                ResultadoUso resultado = repositorio.registrarUso(conteudo);
                Produto p = resultado.getProduto();

                switch (resultado.getStatus()) {
                    case NAO_ENCONTRADO:
                        txtResultado.setText(getString(R.string.produto_nao_cadastrado, conteudo));
                        break;

                    case SEM_ESTOQUE:
                        txtResultado.setText(getString(R.string.produto_sem_estoque, p.getNome()));
                        Toast.makeText(MainActivity.this,
                                getString(R.string.produto_sem_estoque, p.getNome()),
                                Toast.LENGTH_LONG).show();
                        break;

                    case ESTOQUE_BAIXO:
                        String aviso = getString(R.string.produto_estoque_baixo,
                                p.getNome(), p.getQuantidade(), p.getQuantidadeMinima());
                        txtResultado.setText(aviso);
                        Toast.makeText(MainActivity.this, aviso, Toast.LENGTH_LONG).show();
                        break;

                    case OK:
                    default:
                        txtResultado.setText(getString(R.string.produto_uso_ok,
                                p.getNome(), p.getQuantidade()));
                        break;
                }
            }

            @Override
            public void aoCancelar() {
                txtResultado.setText("Leitura cancelada");
            }
        });

        // Botão Cadastrar
        findViewById(R.id.btnCadastrar).setOnClickListener(v -> {
            String codigo = edtCodigo.getText().toString().trim();
            String nome = edtNome.getText().toString().trim();
            String textoQuantidade = edtQuantidade.getText().toString().trim();

            if (codigo.isEmpty() || nome.isEmpty() || textoQuantidade.isEmpty()) {
                Toast.makeText(this, "Preencha código, nome e quantidade",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            int quantidade;
            try {
                quantidade = Integer.parseInt(textoQuantidade);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Quantidade inválida", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean cadastrou = repositorio.cadastrar(new Produto(codigo, nome, quantidade));
            if (cadastrou) {
                Toast.makeText(this, "Produto cadastrado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Já existe um produto com esse código",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Botão Gerar QR
        findViewById(R.id.btnGerar).setOnClickListener(v -> {
            String codigo = edtCodigo.getText().toString().trim();
            Produto produto = repositorio.buscarPorCodigo(codigo);

            if (produto == null) {
                Toast.makeText(this, "Cadastre o produto antes de gerar o QR",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                Bitmap bmp = QrProduto.gerar(produto, 600);
                imgQr.setImageBitmap(bmp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Botão Escanear QR
        findViewById(R.id.btnEscanear).setOnClickListener(v -> scannerHelper.escanear());
    }
}