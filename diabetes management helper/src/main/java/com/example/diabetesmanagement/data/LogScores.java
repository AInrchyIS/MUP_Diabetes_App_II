package com.example.diabetesmanagement.data;

public class LogScores extends Log {

    private static final String TAG ="123" ;
    private int weight;

    public LogScores() {
        setWeight(0);
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
        android.util.Log.d(TAG, "setWeight: "+weight);
    }

    public String logType()
    {
        return "Weight";
    }
}
