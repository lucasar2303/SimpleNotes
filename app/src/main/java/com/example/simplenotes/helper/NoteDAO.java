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
    public List<Note> list() {
        List<Note> notes = new ArrayList<>();

        String sql = "SELECT * FROM " + Dbhelper.TABELA_NOTES + " ;";
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
