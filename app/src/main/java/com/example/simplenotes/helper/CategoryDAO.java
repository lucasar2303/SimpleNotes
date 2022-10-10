package com.example.simplenotes.helper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.simplenotes.model.Category;
import com.example.simplenotes.model.Note;

import java.util.ArrayList;
import java.util.List;

public class CategoryDAO implements ICategoryDAO{

    private SQLiteDatabase write;
    private SQLiteDatabase read;

    public CategoryDAO(Context context){
        Dbhelper dbhelper = new Dbhelper(context);
        write = dbhelper.getWritableDatabase();
        read = dbhelper.getReadableDatabase();
    }
    @Override
    public boolean save(Category category) {
        ContentValues cv = new ContentValues();
        cv.put("name", category.getName());
        try {
            Log.e("INFO", "Sucesso ao salvar Categoria");
            write.insert(Dbhelper.TABELA_CATEGORY, null, cv );
        }catch (Exception e){
            Log.e("INFO", "ERRO AO SALVAR CATEGORIA" + e.getMessage());
            return false;
        }


        return true;
    }

    @Override
    public boolean update(Category category) {
        return false;
    }

    @Override
    public boolean delete(Category category) {
//        try{
//            String[] args =
//        }catch (Exception e){
//            Log.e("INFO", "Erro ao deletar categoria"+ e.getMessage());
//            return false
//        }
        return true;
    }

    @Override
    public List<Category> list() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM " + Dbhelper.TABELA_CATEGORY + " WHERE 1=1 ;";

        Cursor c = read.rawQuery(sql, null);
        while (c.moveToNext()){
            Category category = new Category();

            @SuppressLint("Range") Long id = c.getLong( c.getColumnIndex("id") );
            @SuppressLint("Range") String name = c.getString( c.getColumnIndex("name") );

            category.setId(id);
            category.setName(name);

            categories.add(category);

        }
        return categories;

    }
}