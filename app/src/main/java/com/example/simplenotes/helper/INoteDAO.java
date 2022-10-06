package com.example.simplenotes.helper;

import com.example.simplenotes.model.Note;

import java.util.List;

public interface INoteDAO {
    public boolean save(Note note);
    public boolean update(Note note);
    public boolean delete(Note note);
    public List<Note> list(int filter, String category, Boolean categoryFilter);
    public List<Note> listName(String nameSearch);
}
