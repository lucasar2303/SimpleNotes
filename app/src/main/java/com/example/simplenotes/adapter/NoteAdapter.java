package com.example.simplenotes.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simplenotes.R;
import com.example.simplenotes.model.Note;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.MyViewHolder> {

    private List<Note> listNotes;

    public NoteAdapter(List<Note> list) {
        this.listNotes = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemList = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notes, parent, false);
        return new MyViewHolder(itemList);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Note note = listNotes.get(position);

        holder.tvName.setText(note.getName());
        holder.tvCategory.setText(note.getCategory());
        holder.tvDate.setText(note.getDatecreate());

        String password = note.getPassword();
        if (password==null){
            holder.imgLock.setVisibility(View.GONE);
        }else{

            holder.imgLock.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return this.listNotes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvName, tvDate, tvCategory;
        ImageView imgLock;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvNameNote);
            tvCategory = itemView.findViewById(R.id.tvCategoryNote);
            tvDate = itemView.findViewById(R.id.tvDate);
            imgLock = itemView.findViewById(R.id.imgLock);


        }
    }
}
