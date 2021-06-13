package com.example.checklist.models;

public class item {
    private String item_name;
    private boolean isCompleted;
    private String id;

    public item(){
    }
    public item(String item_name, String id) {
        this.item_name = item_name;
        this.id = id;
        isCompleted = false;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }
}
