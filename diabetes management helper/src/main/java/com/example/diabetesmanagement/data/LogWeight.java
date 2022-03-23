package com.example.diabetesmanagement.data;

public class LogWeight extends Log {

    private static final String TAG ="123" ;
    private double weight;

    public LogWeight() {
        setWeight(0.0);
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
        android.util.Log.d(TAG, "setWeight: "+weight);
    }

    public String logType()
    {
        return "Weight";
    }
}
