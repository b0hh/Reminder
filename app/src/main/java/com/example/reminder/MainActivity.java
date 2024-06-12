package com.example.reminder;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Kullanıcı navigasyon menüsünde Seçim Yaptığında Seçimi Değiştirir ve Seçilen Fragmenti Gösterir
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_notes) {
                    // NotesFragmenti seçer
                    selectedFragment = new NotesFragment();
                } else if (itemId == R.id.navigation_create_note) {
                    // CreateNoteFragmenti seçer
                    selectedFragment = new CreateNoteFragment();
                } else {
                    // ProfileFragmenti seçer
                    selectedFragment = new ProfileFragment();
                }
                // Seçilen fragmenti gösterir
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, selectedFragment).commit();
                return true;
            }
        });

        // Varsayılan Fragmenti Notlar Sayfasi Olarak Ayarlar
        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_notes);
        }
    }
}
