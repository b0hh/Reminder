package com.example.reminder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ReminderDB"; // Veritabanın adı
    private static final int DATABASE_VERSION = 1; // Veritabanın versiyonu

    // Constructor Metodu
    // Veri Tabanı Bağlantısını Başlatır
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*
     *  Veritabanı oluşturulurken Cagirilir
     *  Tabloları oluşturur (eğer tablolar yoksa) ve Toblolardaki Fielderların Özelliklerini Tanımlar.
     *  Oluşturulan Tablolar : Notes, Profile
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS Notes (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, text TEXT, important INTEGER);");
        db.execSQL("CREATE TABLE IF NOT EXISTS Profile (id INTEGER PRIMARY KEY, username TEXT, profilePhotoUri TEXT);");
    }
    // Veritabanı güncellendiğinde çağrılır
    // Mevcut tablolari siler ve yeniden oluşturur
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS Notes");
        db.execSQL("DROP TABLE IF EXISTS Profile");
        onCreate(db);
    }
}
