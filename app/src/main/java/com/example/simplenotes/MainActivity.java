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
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simplenotes.adapter.NoteAdapter;
import com.example.simplenotes.databinding.ActivityMainBinding;
import com.example.simplenotes.helper.Dbhelper;
import com.example.simplenotes.helper.NoteDAO;
import com.example.simplenotes.model.Note;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private int filterSup;

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
        binding.btnFilter.setOnClickListener(view -> showDialogFilter());
        binding.btnSearch.setOnClickListener(view -> newActivty(SearchActivity.class));
        binding.btnAdd.setOnClickListener(view -> newActivty(AddActivity.class));
    }

    @Override
    protected void onStart() {
        setlistNotes();
        super.onStart();
    }

    public void setlistNotes(){
        //Listar tarefas
        NoteDAO noteDAO = new NoteDAO(getApplicationContext());
        listNotes = noteDAO.list();



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
            selecFilter(1);
            alertDialog.dismiss();
        });

        view.findViewById(R.id.tvCreate).setOnClickListener(view1 -> {
            selecFilter(2);
            alertDialog.dismiss();
        });

        view.findViewById(R.id.tvModify).setOnClickListener(view13 -> {
            selecFilter(3);
            alertDialog.dismiss();
        });



        alertDialog.show();
    }

    private void selecFilter(int i){
        filterSup = i;
    }

    private void newActivty(Class c ){
        Intent intent = new Intent(getApplicationContext(), c);
        startActivity(intent);
    }
}