package com.example.dutybook.classes;

public class VoiceUser {
    private String datelast;
    private float ratinglast;
    private Person person;

    public VoiceUser(){}
    public VoiceUser(String datelast, float ratinglast) {
        this.datelast = datelast;
        this.ratinglast = ratinglast;
    }

    public VoiceUser(String datelast, float ratinglast, Person person) {
        this.datelast = datelast;
        this.ratinglast = ratinglast;
        this.person = person;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getDatelast() {
        return datelast;
    }

    public void setDatelast(String datelast) {
        this.datelast = datelast;
    }

    public float getRatinglast() {
        return ratinglast;
    }

    public void setRatinglast(float ratinglast) {
        this.ratinglast = ratinglast;
    }
}
