package com.example.simplenotes.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class Dbhelper extends SQLiteOpenHelper {

    public static int VERSION = 1;
    public static String NOME_DB = "DB_NOTES";
    public static String TABELA_NOTES = "notes";
    public static String TABELA_CATEGORY = "category";

    public Dbhelper(@Nullable Context context) {
        super(context, NOME_DB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlNotes = "CREATE TABLE IF NOT EXISTS " + TABELA_NOTES
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, text TEXT, category TEXT, datecreate DATETIME, datemodify DATETIME, font INT(2), password TEXT ); ";
        String sqlCategory = "CREATE TABLE IF NOT EXISTS " + TABELA_CATEGORY
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT UNIQUE); ";

        try {
            db.execSQL(sqlNotes);
            db.execSQL(sqlCategory);

            Log.i("INFO DB", "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
        }catch (Exception e){
            Log.i("INFO DB", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
