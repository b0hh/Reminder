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

    // Notun tipini belirler (Normal veya Önemli)
    @Override
    public int getItemViewType(int position) {
        // parametre olarak gönderilen pozisyon verisine göre notu getirir
        Note note = notes.get(position);
        // gelen notun tipini belirler
        if (note.isImportant()) {
            return TYPE_IMPORTANT; // Önemli not
        } else {
            return TYPE_NORMAL; // Normal not
        }
    }
    // ViewHolder oluşturur
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // arayüz işlemleri
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // eğer viewType önemli ise öenmli not elemanı oluşturur
        if (viewType == TYPE_IMPORTANT) {
            // itemView değişkenine important_note_item.xml dosyasını atar (arayüz olarak)
            View itemView = inflater.inflate(R.layout.important_note_item, parent, false);
            return new ImportantNoteViewHolder(itemView); // Önemli not viewHolder
        }
        else {
            // itemView değişkenine note_item.xml dosyasını atar (arayüz olarak)
            View itemView = inflater.inflate(R.layout.note_item, parent, false);
            return new NormalNoteViewHolder(itemView); // Normal not viewHolder
        }
    }

    // ViewHolder'a verileri bağlar
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // parametre olarak gönderilen pozisyon verisine göre notu getirir
        Note note = notes.get(position);
        // parametre olarak gönderine viewHolderin item tipine göre note değişkenindeki verileri atar
        // tip önemli ise
        if (holder.getItemViewType() == TYPE_IMPORTANT) {

            ImportantNoteViewHolder importantHolder = (ImportantNoteViewHolder) holder;
            importantHolder.title.setText(note.getTitle());
            importantHolder.text.setText(note.getText());
        }
        // tip normal ise
        else {
            NormalNoteViewHolder normalHolder = (NormalNoteViewHolder) holder;
            normalHolder.title.setText(note.getTitle());
            normalHolder.text.setText(note.getText());
        }
    }
    // Not sayısını döndürür
    @Override
    public int getItemCount() {

        return notes.size();
    }
    // Yeni not ekler
    void addNoteWithoutNotification(Note note) {

        notes.add(note);
    }
    // Verilen pozisyondaki notu döndürür
    Note getNoteAtPosition(int position) {

        return notes.get(position);
    }
    // Verilen pozisyondaki notu siler
    void removeNoteAtPosition(int position) {

        notes.remove(position);
        notifyItemRemoved(position);
    }

    // viewHolderdan miras alan normal not viewHolder classı
    class NormalNoteViewHolder extends RecyclerView.ViewHolder {
        TextView title, text;
        // Constructor metodu title ve text TextViewlarını note_item.xml (onBindViewHolder metodu ile bağlanır) elemanındaki textView elamanlarına atar (başlık ve metin olarak doğru şekilde konumlanmaları için)
        NormalNoteViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.note_title);
            text = itemView.findViewById(R.id.note_text);
        }
    }
    // viewHolderdan miras alan önemli not viewHolder classı
    class ImportantNoteViewHolder extends RecyclerView.ViewHolder {
        TextView title, text;
        // Constructor metodu title ve text TextViewlarını important_note_item.xml (onBindViewHolder metodu ile bağlanır) elemanındaki textView elamanlarına atar (başlık ve metin olarak doğru şekilde konumlanmaları için)        ImportantNoteViewHolder(View itemView) {
        ImportantNoteViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.note_title);
            text = itemView.findViewById(R.id.note_text);
        }
    }
}
