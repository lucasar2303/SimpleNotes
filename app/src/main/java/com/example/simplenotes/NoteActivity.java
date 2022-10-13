package com.example.simplenotes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.splashscreen.SplashScreen;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

        initializeComponents();

        binding.btnBack.setOnClickListener(view -> {
            finish();
        });

        binding.tvCategorySelec.setOnClickListener(view -> categorySelec());

        binding.tvSave.setOnClickListener(view -> {
            updateNote();
            finish();
        });

        binding.btnDelete.setOnClickListener(view -> showDialogDelete());

        binding.btnDownFont.setOnClickListener(view -> fontSize(1));
        binding.btnUpFont.setOnClickListener(view -> fontSize(2));


        binding.btnLock.setOnClickListener(view -> {showDialogPassword();});

        binding.okPassword.setOnClickListener(view -> {
            binding.etPassword.getText().toString();
            if (password.equals(binding.etPassword.getText().toString())){
                binding.layoutPassword.setVisibility(View.GONE);
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }else{
                Toast.makeText(this, "Senha incorreta", Toast.LENGTH_SHORT).show();
            }
        });


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


    private void showDialogPassword(){
        AlertDialog.Builder builder = new AlertDialog.Builder(NoteActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(NoteActivity.this).inflate(R.layout.dialog_password, (LinearLayout)findViewById(R.id.dialogLinearLayout));
        builder.setView(view);

        final AlertDialog alertDialog = builder.create();

        EditText etPassword = (EditText) view.findViewById(R.id.etPassword);
        etPassword.requestFocus();

        view.findViewById(R.id.btnDelete).setOnClickListener(view12 -> {
            password = "";
            updateNote();
            binding.btnLock.setImageTintList(ColorStateList.valueOf(Color.parseColor("#4B4B4B")));
            alertDialog.dismiss();
        });

        view.findViewById(R.id.btnSave).setOnClickListener(view1 -> {

            password = etPassword.getText().toString();
            if (password.length()==4){
                updateNote();
                binding.btnLock.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FF6E40")));
                alertDialog.dismiss();
            }else{
                Toast.makeText(this, "Mínimo de 4 caracteres", Toast.LENGTH_SHORT).show();
            }

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
        NoteDAO noteDAO = new NoteDAO(getApplicationContext());
        noteDAO.delete(note);
    }

    private void showDialogDelete(){
        AlertDialog.Builder builder = new AlertDialog.Builder(NoteActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(NoteActivity.this).inflate(R.layout.dialog_delete, (LinearLayout)findViewById(R.id.dialogLinearLayout));
        builder.setView(view);

        final AlertDialog alertDialog = builder.create();

        TextView title = (TextView) view.findViewById(R.id.tvNoteName);
        title.setText(note.getName());

        view.findViewById(R.id.btnCancel).setOnClickListener(view12 -> {
            alertDialog.dismiss();
        });

        view.findViewById(R.id.btnConfirm).setOnClickListener(view1 -> {
            deleteNote();
            alertDialog.dismiss();
            Toast.makeText(this, "Nota excluída", Toast.LENGTH_SHORT).show();
            finish();
        });

        alertDialog.show();
    }

    private void categorySelec(){
        Intent intent = new Intent(NoteActivity.this, CategorySelectActivity.class);
        intent.putExtra("noteselec", note);

        startActivity(intent);
        finish();

    }

    private void initializeComponents(){
        note = (Note) getIntent().getSerializableExtra("noteselec");
        binding.etTitle.setText(note.getName());
        binding.etText.setText(note.getText());
        fontsize = note.getFont();
        binding.etText.setTextSize(fontsize);
        password = note.getPassword();
        category = note.getCategory();

        if (note.getCategory() != null){
            if (!note.getCategory().equals("")) {
                if (note.getCategory().length()>15){

                }
                binding.tvCategorySelec.setText(note.getCategory());
            }
        }
        Boolean categoryActivity = (Boolean) getIntent().getSerializableExtra("categoryActivity");

        if (categoryActivity==null){
            categoryActivity=false;
        }

        if (categoryActivity){
            if (password != null){
                if (!password.equals("")) {
                    binding.btnLock.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FF6E40")));
                }
            }
        }else{
            if (password != null){
                if (!password.equals("")) {
                    binding.btnLock.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FF6E40")));
                    binding.layoutPassword.setVisibility(View.VISIBLE);
                    binding.etPassword.requestFocus();
                }
            }
        }




    }
}