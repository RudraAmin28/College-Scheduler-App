package com.example.collegeschedulerapp;

public class Task {
    private String title;
    private boolean isChecked;

    public Task(String title, boolean isChecked) {
        this.title = title;
        this.isChecked = isChecked;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isChecked() {
        return isChecked;
    }
}
