package com.example.application;

public class User {
    private String nickname;
    private String email;
    private String uid;
    private String image;


    public User(){
        nickname = "None";
        email = "None";
        image = "None";
        uid = "None";
    }


    public User(String nickname, String email) {
        this.nickname = nickname;
        this.email = email;
    }


    public String getNickname() {
        return nickname;
    }


    public String getEmail() {
        return email;
    }


    public String getUid() {
        return uid;
    }

    public String getImage() { return image; }

    public void setImage(String image) { this.image = image; }

}
