package com.example.dutybook.classes;

import java.util.ArrayList;

public class Duty {
    ArrayList<String> comments = new ArrayList<>();
    String grade;
    Boolean dutynow;
    double rating;
    double numvoice;
    String lastonline;
    double allrating;
    double allnum;

    public Duty(ArrayList<String> comments, String grade, Boolean dutynow, double rating, double numvoice, String lastonline, double allrating, double allnum) {
        this.comments = comments;
        this.grade = grade;
        this.dutynow = dutynow;
        this.rating = rating;
        this.numvoice = numvoice;
        this.lastonline = lastonline;
        this.allrating = allrating;
        this.allnum = allnum;
    }

    public void setNumvoice(double numvoice) {
        this.numvoice = numvoice;
    }

    public double getAllrating() {
        return allrating;
    }

    public void setAllrating(double allrating) {
        this.allrating = allrating;
    }

    public double getAllnum() {
        return allnum;
    }

    public void setAllnum(double allnum) {
        this.allnum = allnum;
    }

    public String getLastonline() {
        return lastonline;
    }

    public void setLastonline(String lastonline) {
        this.lastonline = lastonline;
    }

    public Duty(){}

    public double getNumvoice() {
        return numvoice;
    }


    public Duty(ArrayList<String> comments, String grade, Boolean dutynow, double rating, double numvoice) {
        this.comments = comments;
        this.grade = grade;
        this.dutynow = dutynow;
        this.rating = rating;
        this.numvoice = numvoice;
    }

    public Duty(ArrayList<String> comments, String grade, Boolean dutynow) {
        this.comments = comments;
        this.grade = grade;
        this.dutynow = dutynow;
    }

    public Duty(ArrayList<String> comments, String grade, Boolean dutynow, double rating, double numvoice, String lastonline) {
        this.comments = comments;
        this.grade = grade;
        this.dutynow = dutynow;
        this.rating = rating;
        this.numvoice = numvoice;
        this.lastonline = lastonline;
    }

    public ArrayList<String> getComments() {
        return comments;
    }

    public void setComments(ArrayList<String> comments) {
        this.comments = comments;
    }

    public String getGrade() {
        return grade;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Boolean getDutynow() {
        return dutynow;
    }

    public void setDutynow(Boolean dutynow) {
        this.dutynow = dutynow;
    }
}
