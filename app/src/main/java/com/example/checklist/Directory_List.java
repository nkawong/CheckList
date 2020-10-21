package com.example.checklist;

public class Directory_List {
    private String id;
    private String title_name;

    public Directory_List(){
    }

    public Directory_List(String id, String title_name) {
        this.id = id;
        this.title_name = title_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle_name() {
        return title_name;
    }

    public void setTitle_name(String title_name) {
        this.title_name = title_name;
    }
}
