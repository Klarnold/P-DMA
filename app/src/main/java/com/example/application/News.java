package com.example.application;

public class News {
    private String title;
    private String authorUid;
    private String date;
    private String imageUrl;
    private String description;

    // Конструктор
    public News(String title, String authorUid, String date, String imageUrl, String description) {
        this.title = title;
        this.authorUid = authorUid;
        this.date = date;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    // Геттеры
    public String getTitle() { return title; }
    public String getAuthorUid() { return authorUid; }
    public String getDate() { return date; }
    public String getImageUrl() { return imageUrl; }
    public String getDescription() { return description; }
}