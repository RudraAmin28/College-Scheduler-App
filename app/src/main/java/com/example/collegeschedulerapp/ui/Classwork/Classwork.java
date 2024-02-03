package com.example.collegeschedulerapp.ui.Classwork;

public class Classwork {

    private String name;
    private String classworkType;
    private long dueDateInMillis; // Store due date as milliseconds for simplicity
    private String associatedClass;

    // Constructors, getters, and setters

    public Classwork(String name, String classworkType, long dueDateInMillis, String associatedClass) {
        this.name = name;
        this.classworkType = classworkType;
        this.dueDateInMillis = dueDateInMillis;
        this.associatedClass = associatedClass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassworkType() {
        return classworkType;
    }

    public void setClassworkType(String classworkType) {
        this.classworkType = classworkType;
    }

    public long getDueDateInMillis() {
        return dueDateInMillis;
    }

    public void setDueDateInMillis(long dueDateInMillis) {
        this.dueDateInMillis = dueDateInMillis;
    }

    public String getAssociatedClass() {
        return associatedClass;
    }

    public void setAssociatedClass(String associatedClass) {
        this.associatedClass = associatedClass;
    }
}