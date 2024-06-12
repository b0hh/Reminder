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

        // Veritabanına içine veri girilebilir şekilde bağlanır
        db = new DatabaseHelper(getContext()).getWritableDatabase();

        // Profil verilerini yükler (fonksiyon assağıda açıklandı)
        loadProfile();

        // kullanıcıdan bir resim seçmesini sağlar,
        // seçilen resmin URİ'sini kaydeder ve ardından seçilen resmi arayüzde göstermek için profileİmageView'e atar.
        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                profileImageUri = uri.toString();
                profileImageView.setImageURI(uri);
            }
        });

        // Profil resmine tıklanıldığında çağrılır
        // checkPermişsionAndPickİmage() fonksiyonu çağırır (fonksiyon assağıda açıklandı)
        profileImageView.setOnClickListener(v -> checkPermissionAndPickImage());

        // Güncelle butonuna tıklandığında çağrılır
        // butona tıklandığında kullanıcı adı verişi boş değilse girilen verileri ContentValue(anahtar-değer tutucusu) objesine kaydeder
        // veriler ContentValue objesine kaydedildikten sonra database'e insert edilir
        buttonUpdate.setOnClickListener(v -> {
            // editTextUsername elemanının içindeki veriyi çeker ve String tipine çevirir
            String username = editTextUsername.getText().toString();
            // Çekilen username verişinin değerinin boş olup olmadığını kontrol eder
            if (!username.isEmpty()) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("username", username);
                contentValues.put("profilePhotoUri", profileImageUri);

                // Profili veritabanına ekler ya da günceller
                db.insertWithOnConflict("Profile", null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);

                // Kullanıcıya profilin başarılı bir şekilde güncellendiği hakkında bilgi vermek için kullanılan toast
                Toast.makeText(getContext(), "Profil Güncellendi", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
    // Profili veritabanından yükler
    @SuppressLint("Range") // Cursor objesinin vericeği hataları bastırmak için kullanılır (hata vermemesi için kullanmak dışında pek bir bilgim yok)
    private void loadProfile() {
        // Bütün profilleri çekicek sql query (sadece ilk satırı getirir)
        Cursor cursor = db.rawQuery("SELECT * FROM Profile LIMIT 1", null);

        // cursor.moveToFirst() ilk satır verişini çeker ve cursor objesi ya da false döndürür
        // gelen değer cursor ise username ve profileİmageUri değişkenlerine veritabanından gelen verileri atar
        if (cursor.moveToFirst()) {
            String username = cursor.getString(cursor.getColumnIndex("username"));
            profileImageUri = cursor.getString(cursor.getColumnIndex("profilePhotoUri"));

            // profilde kullanıcı adının gözükmesi için editTextUsername elementine username değerini yazar
            editTextUsername.setText(username);

            // eğer profileİmageUri değişkeni boş değilse (veri tabanından veri gelmişse) değişkendeki değeri imageView elementine atar
            if (profileImageUri != null) {
                profileImageView.setImageURI(Uri.parse(profileImageUri));
            }
        }
        cursor.close();
    }

    // Depolama izni kontrolü ve resim seçimi
    private void checkPermissionAndPickImage() {
        // eğer kullanıcı veri okuma iznine sahip değilse izin sorar
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_READ_EXTERNAL_STORAGE);
        }
        // eğer kullanıcı veri okuma izni vermişse (PERMISSION_GRANTED) pickImage metodunu çağırır
        else {
            pickImage();
        }
    }
    // Resim seçme işlemi
    private void pickImage() {
        // kullanıcının cihazından resim türünde bir veri seçmesi için çağırılır
        mGetContent.launch("image/*");
    }
    // İzin sonucu işleme
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // eğer requestCode daha önce istenen REQUEST_CODE_READ_EXTERNAL_STORAGE izninin koduna eşitse ve izin isteme işleminin geçerli olup olmadığı kontrol eder
        if (requestCode == REQUEST_CODE_READ_EXTERNAL_STORAGE) {
            // eğer grandResults boş değilse ve veri okuma izni verilmişse pickImage metodunu çağırır
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImage();
            }
            // eğer koşullar sağlanmassa kullanıcıya bilgilendirmek için toast yazdırırlır
            else {
                Toast.makeText(getContext(), "Veri okumaya izin verilmedi", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
