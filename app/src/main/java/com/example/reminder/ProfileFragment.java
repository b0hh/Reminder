package com.example.reminder;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.Manifest;
import android.content.pm.PackageManager;


public class ProfileFragment extends Fragment {

    private ImageView profileImageView;
    private EditText editTextUsername;
    private SQLiteDatabase db;
    private String profileImageUri = null;
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 1;

    private ActivityResultLauncher<String> mGetContent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profileImageView = view.findViewById(R.id.profile_image);
        editTextUsername = view.findViewById(R.id.edit_text_username);
        Button buttonUpdate = view.findViewById(R.id.button_update);

        db = new DatabaseHelper(getContext()).getWritableDatabase();

        loadProfile();

        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                profileImageUri = uri.toString();
                profileImageView.setImageURI(uri);
            }
        });

        profileImageView.setOnClickListener(v -> checkPermissionAndPickImage());

        buttonUpdate.setOnClickListener(v -> {
            String username = editTextUsername.getText().toString();
            if (!username.isEmpty()) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("username", username);
                contentValues.put("profilePhotoUri", profileImageUri);

                db.insertWithOnConflict("Profile", null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);

                Toast.makeText(getContext(), "Profil Güncellendi", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @SuppressLint("Range")
    private void loadProfile() {
        Cursor cursor = db.rawQuery("SELECT * FROM Profile LIMIT 1", null);
        if (cursor.moveToFirst()) {
            String username = cursor.getString(cursor.getColumnIndex("username"));
            profileImageUri = cursor.getString(cursor.getColumnIndex("profilePhotoUri"));

            editTextUsername.setText(username);
            if (profileImageUri != null) {
                profileImageView.setImageURI(Uri.parse(profileImageUri));
            }
        }
        cursor.close();
    }

    private void checkPermissionAndPickImage() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_READ_EXTERNAL_STORAGE);
        } else {
            pickImage();
        }
    }

    private void pickImage() {
        mGetContent.launch("image/*");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImage();
            } else {
                Toast.makeText(getContext(), "Permission denied to read external storage", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
