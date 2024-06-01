package com.example.reminder;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import java.util.ArrayList;

public class NotesFragment extends Fragment {

    private NoteAdapter adapter;
    private SQLiteDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NoteAdapter(new ArrayList<>(), getContext());
        recyclerView.setAdapter(adapter);


        db = new DatabaseHelper(getContext()).getWritableDatabase();

        loadNotes();

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Note note = adapter.getNoteAtPosition(position);
                deleteNote(note.getId());
                adapter.removeNoteAtPosition(position);
            }
        }).attachToRecyclerView(recyclerView);

        return view;
    }

    private void loadNotes() {
        Cursor cursor = db.query("Notes", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex("title"));
                @SuppressLint("Range") String text = cursor.getString(cursor.getColumnIndex("text"));
                @SuppressLint("Range") boolean important = cursor.getInt(cursor.getColumnIndex("important")) == 1;
                adapter.addNoteWithoutNotification(new Note(id, title, text, important));
            } while (cursor.moveToNext());
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }

    private void deleteNote(int id) {
        db.delete("Notes", "id=?", new String[]{String.valueOf(id)});
        Toast.makeText(getContext(), "Not Silindi", Toast.LENGTH_SHORT).show();
    }
}
