package com.example.diabetesmanagement.data;

public class LogBloodSugar extends Log {

    private int bloodSugar;
    private boolean beforeMeal;
    private boolean afterMeal;
    private boolean beforeBed;

    public LogBloodSugar() {
        setBloodSugar(0);
        setBeforeMeal(false);
        setAfterMeal(false);
        setBeforeBed(false);
    }

    public int getBloodSugar() {
        return bloodSugar;
    }

    public void setBloodSugar(int bloodSugar) {
        this.bloodSugar = bloodSugar;
    }

    public boolean isBeforeMeal() {
        return beforeMeal;
    }

    public void setBeforeMeal(boolean beforeMeal) {
        this.beforeMeal = beforeMeal;
    }

    public boolean isAfterMeal() {
        return afterMeal;
    }

    public void setAfterMeal(boolean afterMeal) {
        this.afterMeal = afterMeal;
    }

    public boolean isBeforeBed() {
        return beforeBed;
    }

    public void setBeforeBed(boolean beforeBed) {
        this.beforeBed = beforeBed;
    }

    public String logType()
    {
        return "BloodSugar";
    }
}
