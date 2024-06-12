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

        // RecyclerView ve adapter ayarları
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NoteAdapter(new ArrayList<>(), getContext());
        recyclerView.setAdapter(adapter);

        // Veritabanını yazılabilir modda aç
        db = new DatabaseHelper(getContext()).getWritableDatabase();

        // Notları yükle (metod aşşağıda açıklandı)
        loadNotes();

        // Kaydırarak not silme işlemi
        // Kaydırma işlemi için ayarlamalar yapar (0 değeri yukarı assağı kaydırmayı engeller ) (kaydirma yönünü sağ ve sol olarak ayarlar)
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            // notun sürükelnerek yer değiştirmesini engellemek için false değer döndürür
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            // not sağa ya da sola kaydırıldığında kaydırılan notun pozisyonuna göre notu getirir ve id yi deleteNote metoduna göndererek ilgili notu siler, notu adapterdan kaldırır ve recyclerView'ı günceller
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
    // Veritabanından notları yükler
    private void loadNotes() {
        // Notlar tablosundaki verileri bir özelliştirme olmadan çeker (SELECT * FROM Notes koduna eşdeğer)
        Cursor cursor = db.query("Notes", null, null, null, null, null, null);
        // cursor.moveToFirst() Cursor objesi ya da false döner , cursor objesi dönmüşse do while döngüsü ile notları veri tabanından çeker
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
        // veriler değiştiği için RecyclerView'ı günceller
        adapter.notifyDataSetChanged();
    }

    // Belirtilen ID'ye sahip olan notu siler
    private void deleteNote(int id) {
        // notes tablosudan id'si gönderilen id parametresine eşit olan veriyi siler
        db.delete("Notes", "id=?", new String[]{String.valueOf(id)});
        // notun silindiğine dair kullanıcıyı bilgilendirir
        Toast.makeText(getContext(), "Not Silindi", Toast.LENGTH_SHORT).show();
    }
}
