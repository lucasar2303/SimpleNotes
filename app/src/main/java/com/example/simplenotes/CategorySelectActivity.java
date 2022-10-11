package com.example.simplenotes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.simplenotes.adapter.CategoryAdapter;
import com.example.simplenotes.databinding.ActivityCategorySelectBinding;
import com.example.simplenotes.helper.CategoryDAO;
import com.example.simplenotes.helper.NoteDAO;
import com.example.simplenotes.helper.RecyclerItemClickListener;
import com.example.simplenotes.model.Category;
import com.example.simplenotes.model.Note;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CategorySelectActivity extends AppCompatActivity {

    ActivityCategorySelectBinding binding;
    private Note note;

    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;
    private List<Category> listCategory = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        binding = ActivityCategorySelectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recyclerView = binding.recyclerCategory;
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Category category = listCategory.get(position);
                                updateNote(category.getName());

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            }
                        }
                )
        );

        binding.btnAdd.setOnClickListener(view -> showDialogFilter());

        binding.tvRemove.setOnClickListener(view -> {
            updateNote("");
        });

        binding.btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), NoteActivity.class);
            intent.putExtra("noteselec", note);
            startActivity(intent);
            finish();
        });

        note = (Note) getIntent().getSerializableExtra("noteselec");

    }

    @Override
    protected void onStart() {
        setListCategory();
        super.onStart();
    }

    public void setListCategory(){
        //Listar tarefas
        CategoryDAO categoryDAO = new CategoryDAO(getApplicationContext());
        listCategory = categoryDAO.list();



        //Configurar adapter
        categoryAdapter = new CategoryAdapter(listCategory, getApplicationContext());

        //recycler
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(categoryAdapter);
    }

    private void updateNote(String cat){

        NoteDAO noteDAO = new NoteDAO(getApplicationContext());

        Note noteUpdate = new Note();
        noteUpdate.setId(note.getId());
        noteUpdate.setName(note.getName());
        noteUpdate.setText(note.getText());
        noteUpdate.setFont(note.getFont());
        noteUpdate.setPassword(note.getPassword());
        noteUpdate.setCategory(cat);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = new Date();
        String dateString = dateFormat.format(date);
        noteUpdate.setDatemodify(dateString);
        noteUpdate.setDatecreate(note.getDatecreate());

        if (noteDAO.update(noteUpdate)){
            Toast.makeText(this, "Categoria atualizada", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Erro ao atualizar nota", Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent(CategorySelectActivity.this, NoteActivity.class);
        intent.putExtra("noteselec", noteUpdate);
        intent.putExtra("categoryActivity", true);
        startActivity(intent);
        finish();
    }

    private void showDialogFilter(){
        AlertDialog.Builder builder = new AlertDialog.Builder(CategorySelectActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(CategorySelectActivity.this).inflate(R.layout.dialog_new_category, (LinearLayout)findViewById(R.id.dialogLinearLayout));
        builder.setView(view);

        final AlertDialog alertDialog = builder.create();


            view.findViewById(R.id.btnSave).setOnClickListener(view12 -> {
                EditText title = (EditText) view.findViewById(R.id.etName);
                String name = title.getText().toString();

                List<Category> categories = new ArrayList<>();
                CategoryDAO categoryDAO = new CategoryDAO(getApplicationContext());
                categories = categoryDAO.list();
                boolean supName = false;
                for (int u = 0; u<categories.size(); u++){
                    if (categories.get(u).getName().equals(name)) {
                        supName = true;
                    }
                }
                if (supName==false){
                    Category category = new Category();
                    category.setName(name);
                    categoryDAO.save(category);
                    alertDialog.dismiss();
                    setListCategory();
                }else{
                    Toast.makeText(this, "Essa categoria ja existe", Toast.LENGTH_SHORT).show();
                }
            });


        alertDialog.show();
    }
}