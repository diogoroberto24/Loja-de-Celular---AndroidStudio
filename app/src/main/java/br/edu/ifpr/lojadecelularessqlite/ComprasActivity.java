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

public class ComprasActivity extends AppCompatActivity {

    private RecyclerView rvCompras;
    private Button btnNovaCompra, btnVoltarCompras;
    private DatabaseHelper dbHelper;
    private CompraAdapter adapter;
    private ArrayList<Compra> listaCompras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compras);

        rvCompras = findViewById(R.id.rvCompras);
        btnNovaCompra = findViewById(R.id.btnNovaCompra);
        btnVoltarCompras = findViewById(R.id.btnVoltarCompras);

        dbHelper = new DatabaseHelper(this);

        btnNovaCompra.setOnClickListener(v -> {
            Intent i = new Intent(ComprasActivity.this, FormCompraActivity.class);
            startActivity(i);
        });

        btnVoltarCompras.setOnClickListener(v -> finish());

        carregarCompras();
    }

    private void carregarCompras() {
        listaCompras = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT id, id_aparelho, cliente, quantidade, data_compra " +
                        "FROM " + DatabaseHelper.TABELA_COMPRAS,
                null
        );

        if (c.moveToFirst()) {
            do {
                Compra cp = new Compra();
                cp.setId(c.getInt(0));
                cp.setIdAparelho(c.getInt(1));
                cp.setCliente(c.getString(2));
                cp.setQuantidade(c.getInt(3));
                cp.setData(c.getString(4));
                listaCompras.add(cp);
            } while (c.moveToNext());
        }

        c.close();
        db.close();

        adapter = new CompraAdapter(this, listaCompras);
        rvCompras.setLayoutManager(new LinearLayoutManager(this));
        rvCompras.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarCompras();
    }
}