package br.edu.ifpr.lojadecelularessqlite;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AparelhosActivity extends AppCompatActivity {

    private RecyclerView rvAparelhos;
    private Button btnNovoAparelho, btnVoltarAparelhos;
    private DatabaseHelper dbHelper;
    private ArrayList<Aparelho> listaAparelhos;
    private AparelhoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aparelhos);

        rvAparelhos = findViewById(R.id.rvAparelhos);
        btnNovoAparelho = findViewById(R.id.btnNovoAparelho);
        btnVoltarAparelhos = findViewById(R.id.btnVoltarAparelhos);

        dbHelper = new DatabaseHelper(this);

        btnNovoAparelho.setOnClickListener(v -> {
            Intent i = new Intent(AparelhosActivity.this, FormAparelhoActivity.class);
            startActivity(i);
        });

        btnVoltarAparelhos.setOnClickListener(v -> finish());

        carregarAparelhos();
    }

    private void carregarAparelhos() {
        listaAparelhos = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT id, modelo, marca, preco, estoque FROM " + DatabaseHelper.TABELA_APARELHOS,
                null
        );

        if (c.moveToFirst()) {
            do {
                Aparelho a = new Aparelho();
                a.setId(c.getInt(0));
                a.setModelo(c.getString(1));
                a.setMarca(c.getString(2));
                a.setPreco(c.getDouble(3));
                a.setEstoque(c.getInt(4));
                listaAparelhos.add(a);
            } while (c.moveToNext());
        }

        c.close();
        db.close();

        adapter = new AparelhoAdapter(this, listaAparelhos);
        rvAparelhos.setLayoutManager(new LinearLayoutManager(this));
        rvAparelhos.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarAparelhos();
    }
}