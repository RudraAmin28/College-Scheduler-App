package com.example.collegeschedulerapp.ui.Classwork;

public class Classwork {

    private String name;
    private String classworkType;
    private long dueDateInMillis;
    private String associatedClass;
    private String location;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private String time;


    public Classwork(String name, String classworkType, String associatedClass, long dueDateInMillis, String location, String time) {
        this.name = name;
        this.classworkType = classworkType;
        this.dueDateInMillis = dueDateInMillis;
        this.associatedClass = associatedClass;
        this.location = location;
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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