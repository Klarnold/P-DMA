package com.example.application;

public class GameItem {
    private String title;
    private int iconResId;

    public GameItem(String title, int iconResId) {
        this.title = title;
        this.iconResId = iconResId;
    }

    // Геттеры
    public String getTitle() { return title; }
    public int getIconResId() { return iconResId; }
}
