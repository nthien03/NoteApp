package com.example.noteapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> implements Filterable {
    private List<Note> noteList;
    private List<Note> noteListOld;
    private OnClickNote onClickNote;

    public NoteAdapter(List<Note> noteList, OnClickNote onClickNote) {
        this.noteList = noteList;
        this.noteListOld = noteList;
        this.onClickNote = onClickNote;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = noteList.get(position);
        if (note == null) {
            return;
        }
        holder.tvTitle.setText(note.getTitle());
        holder.tvContent.setText(note.getContent());
        holder.tvCreateAt.setText(note.getCreatedAt().toString());

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickNote.onClickBtnEdit(note.getId());
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickNote.onClickBtnDelete(note.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (noteList != null) {
            return noteList.size();
        }
        return 0;
    }
    public void refreshData(List<Note> list){
        noteList = list;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if (strSearch.isEmpty()) {
                    noteList = noteListOld;
                } else {
                    List<Note> list = new ArrayList<>();
                    for (Note note : noteListOld) {
                        if (note.getTitle().toLowerCase().contains(strSearch.toLowerCase()) ||
                            note.getContent().toLowerCase().contains(strSearch.toLowerCase())){
                            list.add(note);
                        }
                    }
                    noteList = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = noteList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                noteList = (List<Note>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle, tvContent, tvCreateAt;
        private ImageButton btnEdit, btnDelete;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvCreateAt = itemView.findViewById(R.id.tv_createAt);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
