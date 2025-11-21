package br.edu.ifpr.lojadecelularessqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "lojacelulares.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABELA_APARELHOS = "aparelhos";
    public static final String TABELA_COMPRAS = "compras";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createAparelhos = "CREATE TABLE " + TABELA_APARELHOS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "modelo TEXT NOT NULL, " +
                "marca TEXT NOT NULL, " +
                "preco REAL NOT NULL, " +
                "estoque INTEGER NOT NULL" +
                ");";

        String createCompras = "CREATE TABLE " + TABELA_COMPRAS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_aparelho INTEGER NOT NULL, " +
                "cliente TEXT NOT NULL, " +
                "quantidade INTEGER NOT NULL, " +
                "data_compra TEXT NOT NULL" +
                ");";

        db.execSQL(createAparelhos);
        db.execSQL(createCompras);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_COMPRAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_APARELHOS);
        onCreate(db);
    }
}