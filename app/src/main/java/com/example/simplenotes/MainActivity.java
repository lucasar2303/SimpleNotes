package com.example.simplenotes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.splashscreen.SplashScreen;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simplenotes.adapter.NoteAdapter;
import com.example.simplenotes.databinding.ActivityMainBinding;
import com.example.simplenotes.helper.CategoryDAO;
import com.example.simplenotes.helper.Dbhelper;
import com.example.simplenotes.helper.NoteDAO;
import com.example.simplenotes.helper.RecyclerItemClickListener;
import com.example.simplenotes.model.Category;
import com.example.simplenotes.model.Note;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private int filterSup=1;
    private String categorySelec = "";
    private Boolean categorySup = false;
    private long categoryId = 0;

    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;
    private List<Note> listNotes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        recyclerView = binding.recyclerNotes;
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Note noteSelec = listNotes.get(position);

                                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                                intent.putExtra("noteselec", noteSelec);

                                startActivity(intent);
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

        binding.btnFilter.setOnClickListener(view -> showDialogFilter());
        binding.btnSearch.setOnClickListener(view -> newActivty(SearchActivity.class));
        binding.btnAdd.setOnClickListener(view -> newActivty(AddActivity.class));
        binding.btnCategory.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        initializeCategory();
        super.onStart();
    }

    public void setlistNotes(String category, Boolean categorySup){
        //Listar tarefas
        NoteDAO noteDAO = new NoteDAO(getApplicationContext());
        listNotes = noteDAO.list(filterSup, category, categorySup);

        //Configurar adapter
        noteAdapter = new NoteAdapter(listNotes);

        //recycler
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(noteAdapter);
    }

    private void showDialogFilter(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_filter, (LinearLayout)findViewById(R.id.dialogLinearLayout));
        builder.setView(view);

        final AlertDialog alertDialog = builder.create();

        switch(filterSup) {
            case 1:
                TextView title = (TextView) view.findViewById(R.id.tvTitle);
                title.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));
                title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_confirm, 0, 0, 0);
                break;
            case 2:
                TextView create = (TextView) view.findViewById(R.id.tvCreate);
                create.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));
                create.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_confirm, 0, 0, 0);
                break;
            case 3:
                TextView modify = (TextView) view.findViewById(R.id.tvModify);
                modify.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));
                modify.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_confirm, 0, 0, 0);
                break;
            default:
                break;
        }



        view.findViewById(R.id.tvTitle).setOnClickListener(view12 -> {
            filterSup = 1;
            setlistNotes(categorySelec, categorySup);
            alertDialog.dismiss();
        });

        view.findViewById(R.id.tvCreate).setOnClickListener(view1 -> {
            filterSup = 2;
            setlistNotes(categorySelec, categorySup);

            alertDialog.dismiss();
        });

        view.findViewById(R.id.tvModify).setOnClickListener(view13 -> {
            filterSup = 3;
            setlistNotes(categorySelec, categorySup);
            alertDialog.dismiss();
        });



        alertDialog.show();
    }

    private void newActivty(Class c ){
        Intent intent = new Intent(getApplicationContext(), c);
        startActivity(intent);
    }

    private void initializeCategory(){
        String categorySelecIntent = (String) getIntent().getSerializableExtra("categorySelect");

        if (getIntent().getSerializableExtra("categoryId")!=null){
            categoryId = (long) getIntent().getSerializableExtra("categoryId");
        }

        if (categorySelecIntent == null){
            setlistNotes(categorySelec, categorySup);
        }else{
            if (categorySelecIntent.equals("")){
                setlistNotes(categorySelec, categorySup);
            }else {

                categorySup = true;
                categorySelec = categorySelecIntent;

                binding.btnDelete.setVisibility(View.VISIBLE);
                binding.btnEdit.setVisibility(View.VISIBLE);
                binding.tvCategorySelec.setText(categorySelec);

                setlistNotes(categorySelec, categorySup);

                binding.btnEdit.setOnClickListener(view -> editCategory());
                binding.btnDelete.setOnClickListener(view -> deleteCategory());

                binding.btnCategory.setOnClickListener(view -> {
                    Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                });

            }
        }

    }

    private void editCategory(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_new_category, (LinearLayout)findViewById(R.id.dialogLinearLayout));
        builder.setView(view);

        final AlertDialog alertDialog = builder.create();

        EditText title = (EditText) view.findViewById(R.id.etName);
        title.setText(categorySelec);
        title.requestFocus();

        view.findViewById(R.id.btnSave).setOnClickListener(view12 -> {
            String nameCategory = title.getText().toString();

            CategoryDAO categoryDAO = new CategoryDAO(getApplicationContext());
            List<Category> categories = categoryDAO.listName(nameCategory);

            if (categories.size()==0){
                for (int i=0;i<listNotes.size();i++){
                    NoteDAO noteDAO = new NoteDAO(getApplicationContext());
                    Note note = new Note();
                    note = listNotes.get(i);
                    note.setCategory(nameCategory);

                    noteDAO.update(note);
                }

                Category category = new Category();
                category.setId(categoryId);
                category.setName(nameCategory);
                categoryDAO.update(category);


                categorySelec = nameCategory;
                setlistNotes(categorySelec, categorySup);

                Toast.makeText(this, "Categoria atualizada", Toast.LENGTH_SHORT).show();

                alertDialog.dismiss();

                finish();
                newActivty(CategoryActivity.class);


            }else{
                Toast.makeText(this, "Essa categoria ja existe", Toast.LENGTH_SHORT).show();
            }

        });

        alertDialog.show();
    }



    private void deleteCategory(){
            if (listNotes.size()==0){
                CategoryDAO categoryDAO = new CategoryDAO(getApplicationContext());
                Category category1 = new Category();
                category1.setName(categorySelec);
                category1.setId(categoryId);
                categoryDAO.delete(category1);
                finish();
                Toast.makeText(this, "Categoria excluída", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Delete todas as notas antes de excluir a categoria", Toast.LENGTH_SHORT).show();
            }

    }
}