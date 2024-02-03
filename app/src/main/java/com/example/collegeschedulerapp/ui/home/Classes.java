package com.example.collegeschedulerapp.ui.home;
public class Classes {
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    private String className;
    private String professor;
    private String startTime;
    private String endTime;

    public Classes(String className, String professor, String startTime, String endTime) {
        this.className = className;
        this.professor = professor;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // ... other methods and getters

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}