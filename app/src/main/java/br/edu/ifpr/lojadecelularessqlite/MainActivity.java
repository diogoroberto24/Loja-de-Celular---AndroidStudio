package br.edu.ifpr.lojadecelularessqlite;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnAparelhos, btnCompras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAparelhos = findViewById(R.id.btnAparelhos);
        btnCompras = findViewById(R.id.btnCompras);

        btnAparelhos.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, AparelhosActivity.class);
            startActivity(i);
        });

        btnCompras.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, ComprasActivity.class);
            startActivity(i);
        });
    }
}