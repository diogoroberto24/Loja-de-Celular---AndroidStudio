package br.edu.ifpr.lojadecelularessqlite;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FormAparelhoActivity extends AppCompatActivity {

    private EditText etModelo, etMarca, etPreco, etEstoque;
    private Button btnSalvarAparelho, btnExcluirAparelho, btnVoltarFormAparelho;
    private DatabaseHelper dbHelper;

    private int idAparelho = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_aparelho);

        etModelo = findViewById(R.id.etModelo);
        etMarca = findViewById(R.id.etMarca);
        etPreco = findViewById(R.id.etPreco);
        etEstoque = findViewById(R.id.etEstoque);

        btnSalvarAparelho = findViewById(R.id.btnSalvarAparelho);
        btnExcluirAparelho = findViewById(R.id.btnExcluirAparelho);
        btnVoltarFormAparelho = findViewById(R.id.btnVoltarFormAparelho);

        dbHelper = new DatabaseHelper(this);

        if (getIntent() != null && getIntent().hasExtra("id")) {
            idAparelho = getIntent().getIntExtra("id", -1);
            String modelo = getIntent().getStringExtra("modelo");
            String marca = getIntent().getStringExtra("marca");
            double preco = getIntent().getDoubleExtra("preco", 0);
            int estoque = getIntent().getIntExtra("estoque", 0);

            etModelo.setText(modelo);
            etMarca.setText(marca);
            etPreco.setText(String.valueOf(preco));
            etEstoque.setText(String.valueOf(estoque));

            btnExcluirAparelho.setEnabled(true);
        } else {
            btnExcluirAparelho.setEnabled(false);
        }

        btnSalvarAparelho.setOnClickListener(v -> salvarAparelho());
        btnExcluirAparelho.setOnClickListener(v -> excluirAparelho());
        btnVoltarFormAparelho.setOnClickListener(v -> finish());
    }

    private void salvarAparelho() {
        String modelo = etModelo.getText().toString().trim();
        String marca = etMarca.getText().toString().trim();
        String precoStr = etPreco.getText().toString().trim();
        String estoqueStr = etEstoque.getText().toString().trim();

        if (TextUtils.isEmpty(modelo) || TextUtils.isEmpty(marca) ||
                TextUtils.isEmpty(precoStr) || TextUtils.isEmpty(estoqueStr)) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        double preco;
        int estoque;
        try {
            preco = Double.parseDouble(precoStr);
            estoque = Integer.parseInt(estoqueStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Preço ou estoque inválidos", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("modelo", modelo);
        valores.put("marca", marca);
        valores.put("preco", preco);
        valores.put("estoque", estoque);

        if (idAparelho == -1) {
            long novoId = db.insert(DatabaseHelper.TABELA_APARELHOS, null, valores);
            if (novoId != -1) {
                Toast.makeText(this, "Aparelho cadastrado!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Erro ao cadastrar", Toast.LENGTH_SHORT).show();
            }
        } else {
            int linhas = db.update(
                    DatabaseHelper.TABELA_APARELHOS,
                    valores,
                    "id = ?",
                    new String[]{String.valueOf(idAparelho)}
            );
            if (linhas > 0) {
                Toast.makeText(this, "Aparelho atualizado!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Erro ao atualizar", Toast.LENGTH_SHORT).show();
            }
        }

        db.close();
        finish();
    }

    private void excluirAparelho() {
        if (idAparelho == -1) {
            Toast.makeText(this, "Nada para excluir", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int linhas = db.delete(
                DatabaseHelper.TABELA_APARELHOS,
                "id = ?",
                new String[]{String.valueOf(idAparelho)}
        );
        db.close();

        if (linhas > 0) {
            Toast.makeText(this, "Aparelho excluído!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Erro ao excluir", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}