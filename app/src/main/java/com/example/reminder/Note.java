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

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public boolean isImportant() {
        return important;
    }
}
