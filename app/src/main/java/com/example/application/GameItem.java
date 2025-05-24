package com.example.application;

import android.os.Parcel;
import android.os.Parcelable;

public class GameItem implements Parcelable{
    private String title;
    private String uid;
    private String date;
    private String imageUrl;
    private String description;
    private String id;
    private int iconUrl;

    public GameItem(){
        this.title = "None";
        this.uid = "None";
        this.date = "None";
        this.imageUrl = "None";
        this.description = "None";
        this.id = "None";
    }

    public GameItem(String id, String title, String uid, String date, String imageUrl, String description) {
        this.title = title;
        this.uid = uid;
        this.date = date;
        this.imageUrl = imageUrl;
        this.description = description;
        this.id = RandomIdGenerator.generateRandomString(12);
    }

    protected GameItem(Parcel in) {
        id = in.readString();
        title = in.readString();
        uid = in.readString();
        date = in.readString();
        imageUrl = in.readString();
        description = in.readString();
    }

    public static final Parcelable.Creator<GameItem> CREATOR = new Parcelable.Creator<GameItem>() {
        @Override
        public GameItem createFromParcel(Parcel in) {
            return new GameItem(in);
        }

        @Override
        public GameItem[] newArray(int size) {
            return new GameItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(uid);
        dest.writeString(date);
        dest.writeString(imageUrl);
        dest.writeString(description);
    }

    public String getTitle() { return title; }
    public String getUid() { return uid; }
    public String getDate() { return date; }
    public String getImageUrl() { return imageUrl; }
    public String getDescription() { return description; }
    public int getIconUrl() { return iconUrl; }
}
