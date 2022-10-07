package com.example.simplenotes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.splashscreen.SplashScreen;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simplenotes.databinding.ActivityNoteBinding;
import com.example.simplenotes.helper.NoteDAO;
import com.example.simplenotes.model.Note;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NoteActivity extends AppCompatActivity {

    private ActivityNoteBinding binding;
    private Note note;

    private int fontsize = 22;
    private String password;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        binding = ActivityNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        note = (Note) getIntent().getSerializableExtra("noteselec");
        binding.etTitle.setText(note.getName());
        binding.etText.setText(note.getText());
        fontsize = note.getFont();
        binding.etText.setTextSize(fontsize);

        binding.tvSave.setOnClickListener(view -> {
            updateNote();
            finish();
        });

        binding.btnDelete.setOnClickListener(view -> {

        });

        password = note.getPassword();
        binding.btnLock.setOnClickListener(view -> {showDialogFilter();});
        if (password != null){
            if (!password.equals("")) {
                binding.btnLock.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FF6E40")));
                binding.layoutPassword.setVisibility(View.VISIBLE);
            }
        }

        Toast.makeText(this, password, Toast.LENGTH_SHORT).show();

        binding.okPassword.setOnClickListener(view -> {
            binding.etPassword.getText().toString();
            if (password.equals(binding.etPassword.getText().toString())){
                binding.layoutPassword.setVisibility(View.GONE);
            }else{
                Toast.makeText(this, "Senha incorreta", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnDownFont.setOnClickListener(view -> fontSize(1));
        binding.btnUpFont.setOnClickListener(view -> fontSize(2));
    }

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


    private void showDialogFilter(){
        AlertDialog.Builder builder = new AlertDialog.Builder(NoteActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(NoteActivity.this).inflate(R.layout.dialog_password, (LinearLayout)findViewById(R.id.dialogLinearLayout));
        builder.setView(view);

        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.btnDelete).setOnClickListener(view12 -> {
            password = "";
            updateNote();
            alertDialog.dismiss();
        });

        view.findViewById(R.id.btnSave).setOnClickListener(view1 -> {

            EditText etPassword = (EditText) view.findViewById(R.id.etPassword);
            password = etPassword.getText().toString();
            updateNote();
            binding.btnLock.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FF6E40")));
            alertDialog.dismiss();
        });

        alertDialog.show();
    }

    private void updateNote(){

        NoteDAO noteDAO = new NoteDAO(getApplicationContext());

        Note noteUpdate = new Note();
        noteUpdate.setId(note.getId());
        noteUpdate.setName(binding.etTitle.getText().toString());
        noteUpdate.setText(binding.etText.getText().toString());
        noteUpdate.setFont(fontsize);
        noteUpdate.setPassword(password);
        noteUpdate.setCategory(category);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = new Date();
        String dateString = dateFormat.format(date);
        noteUpdate.setDatemodify(dateString);
        noteUpdate.setDatecreate(note.getDatecreate());

        if (noteDAO.update(noteUpdate)){
            Toast.makeText(this, "Sucesso ao atualizar nota", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Erro ao atualizar nota", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteNote(){

    }
}