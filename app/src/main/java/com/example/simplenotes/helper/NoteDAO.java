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
        cv.put("font", note.getFont());
        try {

            Log.e("INFO", "ERRO AO SALVAR TAREFEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
            write.insert(Dbhelper.TABELA_NOTES, "password, category", cv );
        }catch (Exception e){
            Log.e("INFO", "ERRO AO SALVAR TAREFAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" + e.getMessage());
            return false;
        }


        return true;
    }

    @Override
    public boolean update(Note note) {
        return false;
    }

    @Override
    public boolean delete(Note note) {
        return false;
    }

    @Override
    public List<Note> list(int filter, String categoryText, Boolean categoryFilter) {
        List<Note> notes = new ArrayList<>();
        String sql = "";

        if (categoryFilter){
            switch (filter){
                case 1:
                    sql = "SELECT * FROM " + Dbhelper.TABELA_NOTES + " WHERE category = "+categoryText+" ORDER BY name ASC ;";
                    break;
                case 2:
                    sql = "SELECT * FROM " + Dbhelper.TABELA_NOTES + " WHERE category = "+categoryText+" ORDER BY datecreate DESC ;";
                    break;
                case 3:
                    sql = "SELECT * FROM " + Dbhelper.TABELA_NOTES + " WHERE category = "+categoryText+" ORDER BY datemodify DESC ;";
                    break;
            }
        }else{
            switch (filter){
                case 1:
                    sql = "SELECT * FROM " + Dbhelper.TABELA_NOTES + " WHERE 1=1 ORDER BY name ASC ;";
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
            @SuppressLint("Range") String password = c.getString( c.getColumnIndex("password") );
            @SuppressLint("Range") String category = c.getString( c.getColumnIndex("category") );

            note.setId(id);
            note.setName(name);
            note.setDatecreate(datecreate);
            note.setCategory(category);
            note.setPassword(password);

            notes.add(note);
        }
        return notes;

    }
}
