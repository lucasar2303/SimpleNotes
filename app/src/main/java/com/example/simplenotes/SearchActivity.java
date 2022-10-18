package com.example.simplenotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.splashscreen.SplashScreen;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simplenotes.adapter.NoteAdapter;
import com.example.simplenotes.databinding.ActivitySearchBinding;
import com.example.simplenotes.helper.NoteDAO;
import com.example.simplenotes.helper.RecyclerItemClickListener;
import com.example.simplenotes.model.Note;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

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
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(binding.recyclerNotes);


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


    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position= viewHolder.getAdapterPosition();
            Note noteSelec = listNotes.get(position);

            showDialogDelete(noteSelec, position);

            View view = SearchActivity.this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

        }
        public void onChildDraw (Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,float dX, float dY,int actionState, boolean isCurrentlyActive){

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(SearchActivity.this, R.color.blackDialog))
                    .addActionIcon(R.drawable.ic_trash)
                    .addSwipeRightLabel("Excluir")
                    .setSwipeRightLabelColor(ContextCompat.getColor(SearchActivity.this, R.color.white))
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    private void showDialogDelete(Note note, int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(SearchActivity.this).inflate(R.layout.dialog_delete, (LinearLayout)findViewById(R.id.dialogLinearLayout));
        builder.setView(view);

        final AlertDialog alertDialog = builder.create();

        TextView title = (TextView) view.findViewById(R.id.tvNoteName);
        title.setText(note.getName());

        view.findViewById(R.id.btnCancel).setOnClickListener(view12 -> {
            noteAdapter.notifyDataSetChanged();
            alertDialog.dismiss();
        });

        view.findViewById(R.id.btnConfirm).setOnClickListener(view1 -> {
            deleteNote(note);
            alertDialog.dismiss();
            listNotes.remove(position);
            noteAdapter.notifyDataSetChanged();
        });

        alertDialog.show();
    }

    private void deleteNote(Note note){
        NoteDAO noteDAO = new NoteDAO(getApplicationContext());
        noteDAO.delete(note);
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