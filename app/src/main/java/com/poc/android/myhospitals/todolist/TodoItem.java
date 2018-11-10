package com.poc.android.myhospitals.todolist;

public class TodoItem {

    private String text;
    private String name;

    public TodoItem() {
    }

    public TodoItem(String text, String name) {
        this.text = text;
        this.name = name;
    }

    public String getText() { return text; }

    public String getName() { return name; }

    public void setText(String text) { this.text = text; }

    public void setName(String name) { this.name = name; }
}
