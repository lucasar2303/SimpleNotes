package com.example.simplenotes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.splashscreen.SplashScreen;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simplenotes.adapter.CategoryAdapter;
import com.example.simplenotes.adapter.NoteAdapter;
import com.example.simplenotes.databinding.ActivityCategoryBinding;
import com.example.simplenotes.helper.CategoryDAO;
import com.example.simplenotes.helper.NoteDAO;
import com.example.simplenotes.helper.RecyclerItemClickListener;
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
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Category category = listCategory.get(position);
                                selectCategory(category.getName(), category.getId());
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

        binding.tvAll.setOnClickListener(view -> selectCategory("",0));

        binding.btnAdd.setOnClickListener(view -> showDialogFilter());

        binding.btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        });
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


    private void showDialogFilter(){
        AlertDialog.Builder builder = new AlertDialog.Builder(CategoryActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(CategoryActivity.this).inflate(R.layout.dialog_new_category, (LinearLayout)findViewById(R.id.dialogLinearLayout));
        builder.setView(view);

        final AlertDialog alertDialog = builder.create();

        EditText title = (EditText) view.findViewById(R.id.etName);
        title.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }

            view.findViewById(R.id.btnSave).setOnClickListener(view12 -> {
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

    private void selectCategory(String category, long categoryId){

        Intent intent = new Intent(CategoryActivity.this, MainActivity.class);
        intent.putExtra("categorySelect", category);
        intent.putExtra("categoryId", categoryId);
        startActivity(intent);
        finish();
    }

}