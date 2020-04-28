package com.example.dutybook.classes;

import java.util.HashMap;

public class Person {
    private String name;
    private String grade;
    private String datelast;
    private int numdelay;
    private int id;
    private HashMap<String,String> delays;
    private boolean auth;



    public Person(String name, String grade, String datelast, int numdelay, boolean auth, HashMap<String,String> delays) {
        this.name = name;
        this.grade = grade;
        this.datelast = datelast;
        this.numdelay = numdelay;
        this.auth = auth;
        this.delays = delays;
    }

    public Person(String name, int numdelay) {
        this.numdelay = numdelay;
        this.name = name;
    }

    public Person(){}
    public Person(String name, String grade, String datelast, int numdelay, int id) {
        this.name = name;
        this.grade = grade;
        this.datelast = datelast;
        this.numdelay = numdelay;
        this.id = id;
    }

    public Person(String name, int i, String grade, int numdelay, int id) {
        this.name = name;
        this.grade = grade;
        ///data must do!!!
        this.numdelay = 0;
    }

    public String getName() {
        return name;
    }

    public String getGrade() {
        return grade;
    }

    public String getDatelast() {
        return datelast;
    }

    public int getNumdelay() {
        return numdelay;
    }
    public void setId(int id) {
        this.id = id;
    }

    public HashMap<String, String> getDelays() {
        return delays;
    }

    public void setDelays(HashMap<String, String> delays) {
        this.delays = delays;
    }

    public boolean isAuth() {
        return auth;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
    }
    public String getNumdelayString() {
        return Integer.toString(this.numdelay);
    }

    public int getId() {
        return id;
    }
    public String getIdtoString() {
        return Integer.toString(this.id);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setDatelast(String datelast) {
        this.datelast = datelast;
    }

    public void setNumdelay(int numdelay) {
        this.numdelay = numdelay;
    }
}


