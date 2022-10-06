package com.example.simplenotes.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simplenotes.CategoryActivity;
import com.example.simplenotes.R;
import com.example.simplenotes.helper.CategoryDAO;
import com.example.simplenotes.helper.NoteDAO;
import com.example.simplenotes.model.Category;
import com.example.simplenotes.model.Note;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private List<Category> listCategory;
    private Context context;

    public CategoryAdapter(List<Category> list, Context context) {
        this.listCategory = list;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemList = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryAdapter.MyViewHolder(itemList);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.MyViewHolder holder, int position) {

        Category category = listCategory.get(position);

        holder.tvName.setText(category.getName());
//        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                CategoryDAO categoryDAO = new CategoryDAO(context);
//                NoteDAO noteDAO = new NoteDAO(context);
//                String nameCategory = holder.tvName.getText().toString();
//                boolean categorySup = false;
//
//                List<Note> listNotes;
//                listNotes = noteDAO.list(1, "", false);
//
//
//                for (int i = 0 ; i<listNotes.size(); i++){
//                    if (nameCategory.equals(listNotes.get(i).getName())){
//                        Toast.makeText(context, "Delete todas as notas dessa categoria primeiro", Toast.LENGTH_SHORT).show();
//                        categorySup = true;
//                    }
//                    if (!categorySup){
//                        categoryDAO.delete(category);
//                    }
//                }
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return this.listCategory.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvName;
        ImageView imgDelete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvNameCategory);
            imgDelete = itemView.findViewById(R.id.imgDelete);

        }
    }
}
