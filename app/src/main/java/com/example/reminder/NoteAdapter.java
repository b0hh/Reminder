package com.example.reminder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_IMPORTANT = 1;

    private ArrayList<Note> notes;
    private Context context;

    NoteAdapter(ArrayList<Note> notes, Context context) {
        this.notes = notes;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        Note note = notes.get(position);
        if (note.isImportant()) {
            return TYPE_IMPORTANT;
        } else {
            return TYPE_NORMAL;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_IMPORTANT) {
            View itemView = inflater.inflate(R.layout.important_note_item, parent, false);
            return new ImportantNoteViewHolder(itemView);
        } else {
            View itemView = inflater.inflate(R.layout.note_item, parent, false);
            return new NormalNoteViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Note note = notes.get(position);
        if (holder.getItemViewType() == TYPE_IMPORTANT) {
            ImportantNoteViewHolder importantHolder = (ImportantNoteViewHolder) holder;
            importantHolder.title.setText(note.getTitle());
            importantHolder.text.setText(note.getText());
        } else {
            NormalNoteViewHolder normalHolder = (NormalNoteViewHolder) holder;
            normalHolder.title.setText(note.getTitle());
            normalHolder.text.setText(note.getText());
        }
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    void addNoteWithoutNotification(Note note) {
        notes.add(note);
    }

    Note getNoteAtPosition(int position) {
        return notes.get(position);
    }

    void removeNoteAtPosition(int position) {
        notes.remove(position);
        notifyItemRemoved(position);
    }

    class NormalNoteViewHolder extends RecyclerView.ViewHolder {
        TextView title, text;

        NormalNoteViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.note_title);
            text = itemView.findViewById(R.id.note_text);
        }
    }

    class ImportantNoteViewHolder extends RecyclerView.ViewHolder {
        TextView title, text;

        ImportantNoteViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.note_title);
            text = itemView.findViewById(R.id.note_text);
        }
    }
}
