package com.example.simplenotes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.splashscreen.SplashScreen;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simplenotes.adapter.CategoryAdapter;
import com.example.simplenotes.adapter.NoteAdapter;
import com.example.simplenotes.databinding.ActivityCategoryBinding;
import com.example.simplenotes.helper.CategoryDAO;
import com.example.simplenotes.helper.NoteDAO;
import com.example.simplenotes.model.Category;
import com.example.simplenotes.model.Note;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    ActivityCategoryBinding binding;

    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;
    private List<Category> listCategory = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recyclerView = binding.recyclerCategory;

        binding.btnAdd.setOnClickListener(view -> {
            showDialogFilter(1);});

        binding.btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onStart() {
        setlistNotes();
        super.onStart();
    }


    public void setlistNotes(){
        //Listar tarefas
        CategoryDAO categoryDAO = new CategoryDAO(getApplicationContext());
        listCategory = categoryDAO.list();

        Log.e("INFO", listCategory.get(0).getName().toString());

        //Configurar adapter
        categoryAdapter = new CategoryAdapter(listCategory, getApplicationContext());

        //recycler
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(categoryAdapter);
    }


    private void showDialogFilter(int i){
        AlertDialog.Builder builder = new AlertDialog.Builder(CategoryActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(CategoryActivity.this).inflate(R.layout.dialog_new_category, (LinearLayout)findViewById(R.id.dialogLinearLayout));
        builder.setView(view);

        final AlertDialog alertDialog = builder.create();

        if (i == 1){
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
                    setlistNotes();
                }else{
                    Toast.makeText(this, "Essa categoria ja existe", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (i == 2){

            view.findViewById(R.id.btnSave).setOnClickListener(view1 -> {

            });
        }

        alertDialog.show();
    }

}