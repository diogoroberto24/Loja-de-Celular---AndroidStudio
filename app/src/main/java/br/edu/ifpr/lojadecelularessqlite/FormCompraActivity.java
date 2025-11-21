package br.edu.ifpr.lojadecelularessqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FormCompraActivity extends AppCompatActivity {

    private EditText etIdAparelho, etCliente, etQuantidade, etData;
    private Button btnSalvarCompra, btnExcluirCompra, btnVoltarFormCompra;
    private DatabaseHelper dbHelper;

    private int idCompra = -1;
    private int idAparelhoOriginal = -1;
    private int quantidadeOriginal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_compra);

        etIdAparelho = findViewById(R.id.etIdAparelho);
        etCliente = findViewById(R.id.etCliente);
        etQuantidade = findViewById(R.id.etQuantidade);
        etData = findViewById(R.id.etData);

        btnSalvarCompra = findViewById(R.id.btnSalvarCompra);
        btnExcluirCompra = findViewById(R.id.btnExcluirCompra);
        btnVoltarFormCompra = findViewById(R.id.btnVoltarFormCompra);

        dbHelper = new DatabaseHelper(this);

        // MODO EDIÇÃO
        if (getIntent() != null && getIntent().hasExtra("id")) {
            idCompra = getIntent().getIntExtra("id", -1);
            idAparelhoOriginal = getIntent().getIntExtra("id_aparelho", -1);
            String cliente = getIntent().getStringExtra("cliente");
            quantidadeOriginal = getIntent().getIntExtra("quantidade", 0);
            String data = getIntent().getStringExtra("data");

            etIdAparelho.setText(String.valueOf(idAparelhoOriginal));
            etCliente.setText(cliente);
            etQuantidade.setText(String.valueOf(quantidadeOriginal));
            etData.setText(data);

            // Para simplificar a lógica de estoque, bloqueia edição de id e quantidade
            etIdAparelho.setEnabled(false);
            etQuantidade.setEnabled(false);

            btnExcluirCompra.setEnabled(true);
        } else {
            btnExcluirCompra.setEnabled(false);
        }

        btnSalvarCompra.setOnClickListener(v -> salvarCompra());
        btnExcluirCompra.setOnClickListener(v -> excluirCompra());
        btnVoltarFormCompra.setOnClickListener(v -> finish());
    }

    private void salvarCompra() {

        String idStr = etIdAparelho.getText().toString().trim();
        String cliente = etCliente.getText().toString().trim();
        String qtdStr = etQuantidade.getText().toString().trim();
        String data = etData.getText().toString().trim();

        if (TextUtils.isEmpty(idStr) || TextUtils.isEmpty(cliente) ||
                TextUtils.isEmpty(qtdStr) || TextUtils.isEmpty(data)) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        int idAp = Integer.parseInt(idStr);
        int qtd = Integer.parseInt(qtdStr);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        if (idCompra == -1) {
            // NOVA COMPRA – C (Create) + controle de estoque

            // 1) Buscar estoque atual
            Cursor c = db.rawQuery(
                    "SELECT estoque FROM " + DatabaseHelper.TABELA_APARELHOS + " WHERE id = ?",
                    new String[]{String.valueOf(idAp)}
            );

            if (!c.moveToFirst()) {
                Toast.makeText(this, "Aparelho não encontrado!", Toast.LENGTH_SHORT).show();
                c.close();
                db.close();
                return;
            }

            int estoqueAtual = c.getInt(0);
            c.close();

            if (qtd > estoqueAtual) {
                Toast.makeText(
                        this,
                        "Estoque insuficiente! Estoque atual: " + estoqueAtual,
                        Toast.LENGTH_LONG
                ).show();
                db.close();
                return;
            }

            // 2) Atualizar estoque
            int novoEstoque = estoqueAtual - qtd;

            ContentValues valoresEstoque = new ContentValues();
            valoresEstoque.put("estoque", novoEstoque);

            db.update(
                    DatabaseHelper.TABELA_APARELHOS,
                    valoresEstoque,
                    "id = ?",
                    new String[]{String.valueOf(idAp)}
            );

            // 3) Registrar compra
            ContentValues valores = new ContentValues();
            valores.put("id_aparelho", idAp);
            valores.put("cliente", cliente);
            valores.put("quantidade", qtd);
            valores.put("data_compra", data);

            long novoId = db.insert(DatabaseHelper.TABELA_COMPRAS, null, valores);
            db.close();

            if (novoId != -1) {
                Toast.makeText(this, "Compra registrada com sucesso!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Erro ao registrar compra!", Toast.LENGTH_SHORT).show();
            }

        } else {
            // ATUALIZAÇÃO – U (Update) (somente cliente e data, sem mexer no estoque)

            ContentValues valores = new ContentValues();
            valores.put("cliente", cliente);
            valores.put("data_compra", data);

            int linhas = db.update(
                    DatabaseHelper.TABELA_COMPRAS,
                    valores,
                    "id = ?",
                    new String[]{String.valueOf(idCompra)}
            );

            db.close();

            if (linhas > 0) {
                Toast.makeText(this, "Compra atualizada!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Erro ao atualizar compra!", Toast.LENGTH_SHORT).show();
            }
        }

        finish();
    }

    private void excluirCompra() {

        if (idCompra == -1) {
            Toast.makeText(this, "Nada para excluir", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // devolve qtd ao estoque do aparelho
        if (idAparelhoOriginal != -1 && quantidadeOriginal > 0) {
            Cursor c = db.rawQuery(
                    "SELECT estoque FROM " + DatabaseHelper.TABELA_APARELHOS + " WHERE id = ?",
                    new String[]{String.valueOf(idAparelhoOriginal)}
            );

            if (c.moveToFirst()) {
                int estoqueAtual = c.getInt(0);
                int novoEstoque = estoqueAtual + quantidadeOriginal;

                ContentValues valoresEstoque = new ContentValues();
                valoresEstoque.put("estoque", novoEstoque);

                db.update(
                        DatabaseHelper.TABELA_APARELHOS,
                        valoresEstoque,
                        "id = ?",
                        new String[]{String.valueOf(idAparelhoOriginal)}
                );
            }
            c.close();
        }

        int linhas = db.delete(
                DatabaseHelper.TABELA_COMPRAS,
                "id = ?",
                new String[]{String.valueOf(idCompra)}
        );

        db.close();

        if (linhas > 0) {
            Toast.makeText(this, "Compra excluída!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Erro ao excluir compra!", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}