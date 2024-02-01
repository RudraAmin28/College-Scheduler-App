package com.example.collegeschedulerapp.ui.home;
public class Classes {
    private String className;
    private String professor;

    public Classes(String title, String professor) {
        this.className = title;
        this.professor = professor;
    }

    public void setClassName(String title) {
        this.className = title;
    }

    public String getclassName() {
        return className;
    }

    public void setProfessor(String title) {
        this.professor = title;
    }

    public String getProfessor() {
        return professor;
    }

}
