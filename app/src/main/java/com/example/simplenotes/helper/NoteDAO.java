package com.example.simplenotes.helper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.simplenotes.model.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteDAO implements INoteDAO{

    private SQLiteDatabase write;
    private SQLiteDatabase read;

    public NoteDAO(Context context){
        Dbhelper dbhelper = new Dbhelper(context);
        write = dbhelper.getWritableDatabase();
        read = dbhelper.getReadableDatabase();
    }


    @Override
    public boolean save(Note note) {
        ContentValues cv = new ContentValues();
        cv.put("name", note.getName());
        cv.put("text", note.getText());
        cv.put("datecreate", note.getDatecreate());
        cv.put("datemodify", note.getDatemodify());
        cv.put("category", note.getCategory());
        cv.put("font", note.getFont());
        try {

            Log.e("INFO", "SUCESSO AO SALVAR NOTA");
            write.insert(Dbhelper.TABELA_NOTES, "password, category", cv );
        }catch (Exception e){
            Log.e("INFO", "ERRO AO SALVAR NOTA" + e.getMessage());
            return false;
        }


        return true;
    }

    @Override
    public boolean update(Note note) {

        ContentValues cv = new ContentValues();
        cv.put("name", note.getName());
        cv.put("text", note.getText());
        cv.put("datecreate", note.getDatecreate());
        cv.put("datemodify", note.getDatemodify());
        cv.put("font", note.getFont());
        cv.put("category", note.getCategory());
        cv.put("password", note.getPassword());

        try {
            String[] args = {note.getId().toString()};
            write.update(Dbhelper.TABELA_NOTES, cv, "id=?", args);
            Log.e("INFO", "SUCESSO AO ATUALIZAR NOTA");
        }catch (Exception e){
            Log.e("INFO", "ERRO AO ATUALIZAR NOTA" + e.getMessage());
            return false;
        }


        return true;
    }

    @Override
    public boolean delete(Note note) {


        try {
            String[] args = {note.getId().toString()};
            write.delete(Dbhelper.TABELA_NOTES, "id=?", args);
            Log.e("INFO", "SUCESSO AO DELETAR NOTA");
        }catch (Exception e){
            Log.e("INFO", "ERRO AO DELETAR NOTA" + e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public List<Note> list(int filter, String categoryText, Boolean categoryFilter) {
        List<Note> notes = new ArrayList<>();
        String sql = "";

        if (categoryFilter){
            switch (filter){
                case 1:
                    sql = "SELECT * FROM " + Dbhelper.TABELA_NOTES + " WHERE category = '"+categoryText+"' ORDER BY LOWER(name) ASC ;";
                    break;
                case 2:
                    sql = "SELECT * FROM " + Dbhelper.TABELA_NOTES + " WHERE category = '"+categoryText+"' ORDER BY datecreate DESC ;";
                    break;
                case 3:
                    sql = "SELECT * FROM " + Dbhelper.TABELA_NOTES + " WHERE category = '"+categoryText+"' ORDER BY datemodify DESC ;";
                    break;
            }
        }else{
            switch (filter){
                case 1:
                    sql = "SELECT * FROM " + Dbhelper.TABELA_NOTES + " WHERE 1=1 ORDER BY LOWER(name) ASC ;";
                    break;
                case 2:
                    sql = "SELECT * FROM " + Dbhelper.TABELA_NOTES + " WHERE 1=1 ORDER BY datecreate DESC ;";
                    break;
                case 3:
                    sql = "SELECT * FROM " + Dbhelper.TABELA_NOTES + " WHERE 1=1 ORDER BY datemodify DESC ;";
                    break;
            }
        }


        Cursor c = read.rawQuery(sql, null);
        while (c.moveToNext()){
            Note note = new Note();

            @SuppressLint("Range") Long id = c.getLong( c.getColumnIndex("id") );
            @SuppressLint("Range") String name = c.getString( c.getColumnIndex("name") );
            @SuppressLint("Range") String datecreate = c.getString( c.getColumnIndex("datecreate") );
            @SuppressLint("Range") String datemodify = c.getString( c.getColumnIndex("datemodify") );
            @SuppressLint("Range") String password = c.getString( c.getColumnIndex("password") );
            @SuppressLint("Range") String category = c.getString( c.getColumnIndex("category") );
            @SuppressLint("Range") String text = c.getString( c.getColumnIndex("text") );
            @SuppressLint("Range") int font = c.getInt( c.getColumnIndex("font") );

            note.setId(id);
            note.setName(name);
            note.setDatecreate(datecreate);
            note.setCategory(category);
            note.setPassword(password);
            note.setText(text);
            note.setDatemodify(datemodify);
            note.setFont(font);

            notes.add(note);

        }
        return notes;

    }

    public List<Note> listName(String nameSearch) {
        List<Note> notes = new ArrayList<>();
        String sql = "";

        if (!nameSearch.equals("")){
            sql = "SELECT * FROM " + Dbhelper.TABELA_NOTES + " WHERE name LIKE '%"+ nameSearch +"%'  ORDER BY name ASC ;";
        }else{
            sql = "SELECT * FROM " + Dbhelper.TABELA_NOTES + " WHERE 1=2 ORDER BY name ASC ;";
        }

        Cursor c = read.rawQuery(sql, null);
        while (c.moveToNext()){
            Note note = new Note();

            @SuppressLint("Range") Long id = c.getLong( c.getColumnIndex("id") );
            @SuppressLint("Range") String name = c.getString( c.getColumnIndex("name") );
            @SuppressLint("Range") String datecreate = c.getString( c.getColumnIndex("datecreate") );
            @SuppressLint("Range") String datemodify = c.getString( c.getColumnIndex("datemodify") );
            @SuppressLint("Range") String password = c.getString( c.getColumnIndex("password") );
            @SuppressLint("Range") String category = c.getString( c.getColumnIndex("category") );
            @SuppressLint("Range") String text = c.getString( c.getColumnIndex("text") );
            @SuppressLint("Range") int font = c.getInt( c.getColumnIndex("font") );

            note.setId(id);
            note.setName(name);
            note.setDatecreate(datecreate);
            note.setCategory(category);
            note.setPassword(password);
            note.setText(text);
            note.setDatemodify(datemodify);
            note.setFont(font);

            notes.add(note);

        }
        return notes;

    }
}
