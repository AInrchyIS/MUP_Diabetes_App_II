package com.example.diabetesmanagement.data;

public class LogMood extends Log {

    private String physicalMood;
    private String mentalMood;
    private String mood;

    public LogMood() {
        setPhysicalMood("");
        setMentalMood("");
        setMood("");
    }

    public String getPhysicalMood() {
        return physicalMood;
    }

    public void setPhysicalMood(String physicalMood) {
        this.physicalMood = physicalMood;
    }

    public String getMentalMood() {
        return mentalMood;
    }

    public void setMentalMood(String mentalMood) {
        this.mentalMood = mentalMood;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String logType()
    {
        return "Mood";
    }
}
