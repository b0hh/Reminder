package com.example.reminder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class  DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ReminderDB";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Notes (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, text TEXT, important INTEGER);");
        db.execSQL("CREATE TABLE IF NOT EXISTS Profile (id INTEGER PRIMARY KEY, username TEXT, profilePhotoUri TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Notes");
        db.execSQL("DROP TABLE IF EXISTS Profile");
        onCreate(db);
    }
}
