package com.example.simplenotes.helper;

import com.example.simplenotes.model.Category;
import com.example.simplenotes.model.Note;

import java.util.List;

public interface ICategoryDAO {
    public boolean save(Category category);
    public boolean update(Category category);
    public boolean delete(Category category);
    public List<Category> list();
}
