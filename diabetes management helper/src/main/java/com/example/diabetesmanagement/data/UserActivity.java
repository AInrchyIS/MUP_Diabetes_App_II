package com.example.diabetesmanagement.data;

import com.google.firebase.Timestamp;

public class UserActivity {

    private String activity;
    private Timestamp timestamp;
    private String uid;



    public UserActivity()
    {

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
