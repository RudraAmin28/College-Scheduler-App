package com.example.collegeschedulerapp.ui.ToDo;

public class Task {
    private String title;
    private String category;
    private boolean isChecked;

    public Task(String title, String category, boolean isChecked) {
        this.title = title;
        this.category = category;
        this.isChecked = isChecked;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setCategory(String category) {this.category = category;}

    public String getCategory() {return category;}

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isChecked() {
        return isChecked;
    }
}
