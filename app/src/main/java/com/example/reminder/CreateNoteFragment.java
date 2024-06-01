package com.example.reminder;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class CreateNoteFragment extends Fragment {

    private EditText editTextTitle, editTextText;
    private CheckBox checkBoxImportant;
    private SQLiteDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_note, container, false);

        editTextTitle = view.findViewById(R.id.edit_text_title);
        editTextText = view.findViewById(R.id.edit_text_text);
        checkBoxImportant = view.findViewById(R.id.check_box_important);
        Button buttonSave = view.findViewById(R.id.button_save);

        db = new DatabaseHelper(getContext()).getWritableDatabase();

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTextTitle.getText().toString();
                String text = editTextText.getText().toString();
                boolean isImportant = checkBoxImportant.isChecked();

                if (!title.isEmpty() && !text.isEmpty()) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("title", title);
                    contentValues.put("text", text);
                    contentValues.put("important", isImportant ? 1 : 0);
                    db.insert("Notes", null, contentValues);

                    Toast.makeText(getContext(), "Not Kaydedildi", Toast.LENGTH_SHORT).show();
                    editTextTitle.setText("");
                    editTextText.setText("");
                    checkBoxImportant.setChecked(false);
                }
            }
        });

        return view;
    }
}
