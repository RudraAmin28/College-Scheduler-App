package com.example.collegeschedulerapp.ui.home;

import java.util.List;

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
    private List<String> selectedDays;

    public List<String> getSelectedDays() {
        return selectedDays;
    }

    public void setSelectedDays(List<String> selectedDays) {
        this.selectedDays = selectedDays;
    }


    public Classes(String className, String professor, String startTime, String endTime, List<String> selectedDays) {
        this.className = className;
        this.professor = professor;
        this.startTime = startTime;
        this.endTime = endTime;
        this.selectedDays = selectedDays;
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