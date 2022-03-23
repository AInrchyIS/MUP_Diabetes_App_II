package com.example.diabetesmanagement.data;

public class LogActivity extends Log {

    private int duration;
    private String description;

    public LogActivity() {
        setDuration(0);
        setDescription("");
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String logType()
    {
        return "Activity";
    }
}
