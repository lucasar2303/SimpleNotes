package com.example.simplenotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import com.example.simplenotes.databinding.ActivityAddBinding;
import com.example.simplenotes.databinding.ActivitySearchBinding;
import com.example.simplenotes.helper.NoteDAO;
import com.example.simplenotes.model.Note;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddActivity extends AppCompatActivity {

    private ActivityAddBinding binding;
    private int fontsize = 22;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        binding = ActivityAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getIntent().getSerializableExtra("category")!=null){
            category = (String) getIntent().getSerializableExtra("category");
        }


        binding.btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        });

        binding.tvSave.setOnClickListener(view -> checkfields());
        binding.btnDownFont.setOnClickListener(view -> fontSize(1));
        binding.btnUpFont.setOnClickListener(view -> fontSize(2));
    }

    private void saveNote(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = new Date();
        String dateString = dateFormat.format(date);

        NoteDAO noteDAO = new NoteDAO(getApplicationContext());
        Note note = new Note();
        note.setName(binding.etTitle.getText().toString());
        note.setText(binding.etText.getText().toString());
        note.setFont(fontsize);
        note.setDatecreate(dateString);
        note.setDatemodify(dateString);




        if (!category.equals("")){
            note.setCategory(category);
            Toast.makeText(this, category, Toast.LENGTH_SHORT).show();
        }

        noteDAO.save(note);

        finish();
    }

    private void checkfields(){
        String title = binding.etTitle.getText().toString();
        if (!title.equals("")){
            saveNote();
        }else{
            Toast.makeText(this, "Digite um t??tulo", Toast.LENGTH_SHORT).show();
        }

    };

    private void fontSize(int i){
        if (i==1){
            if (fontsize==16){
                binding.etText.setTextSize(28);
                fontsize=28;
            }else{
                binding.etText.setTextSize(fontsize-2);
                fontsize=fontsize-2;
            }
        }else{
            if (fontsize==28){
                binding.etText.setTextSize(16);
                fontsize=16;
            }else{
                binding.etText.setTextSize(fontsize+2);
                fontsize=fontsize+2;
            }
        }
    }
}