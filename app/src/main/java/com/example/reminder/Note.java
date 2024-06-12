package com.example.reminder;

public class Note {
    private int id;
    private String title;
    private String text;
    private boolean important;

    public Note(int id, String title, String text, boolean important) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.important = important;
    }
    // Not ID'sini döndürür
    public int getId() {
        return id;
    }

    // Not başlığını döndürür
    public String getTitle() {
        return title;
    }

    // Not metnini döndürür
    public String getText() {
        return text;
    }

    // Notun önemli olup olmadığını döndürür
    public boolean isImportant() {
        return important;
    }
}
