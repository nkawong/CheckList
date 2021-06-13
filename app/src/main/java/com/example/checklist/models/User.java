package com.example.checklist.models;

public class User {
    String user_id;
    String email;

    public User(String user_id, String email) {
        this.user_id = user_id;
        this.email = email;
    }

    public User() {
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id='" + user_id + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
