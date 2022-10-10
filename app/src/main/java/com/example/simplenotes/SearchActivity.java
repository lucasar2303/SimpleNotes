package com.example.simplenotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.simplenotes.adapter.NoteAdapter;
import com.example.simplenotes.databinding.ActivitySearchBinding;
import com.example.simplenotes.helper.NoteDAO;
import com.example.simplenotes.helper.RecyclerItemClickListener;
import com.example.simplenotes.model.Note;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private ActivitySearchBinding binding;

    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;
    private List<Note> listNotes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
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

                                Intent intent = new Intent(SearchActivity.this, NoteActivity.class);
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

        binding.btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        });

        binding.btnClear.setOnClickListener(view -> {
            binding.etSearch.setText("");
        });

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkClear();
                setlistNotes(binding.etSearch.getText().toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void setlistNotes(String nameSearch){
        //Listar tarefas
        NoteDAO noteDAO = new NoteDAO(getApplicationContext());
        listNotes = noteDAO.listName(nameSearch);

        //Configurar adapter
        noteAdapter = new NoteAdapter(listNotes);

        //recycler
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(noteAdapter);
    }

    private void checkClear(){
        if (!binding.etSearch.getText().toString().isEmpty()){
            binding.btnClear.setVisibility(View.VISIBLE);
        }else{
            binding.btnClear.setVisibility(View.GONE);
        }
    }
}