package com.example.diabetesmanagement.data;

import java.io.Serializable;

public class Medication implements Serializable {

    private String name;
    private String userID;
    private int dailyDoses;

    public Medication(){
        setName("");
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDailyDoses() {
        return dailyDoses;
    }

    public void setDailyDoses(int dailyDoses) {
        this.dailyDoses = dailyDoses;
    }
}
