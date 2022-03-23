package com.example.diabetesmanagement.data;

public class LogInsulin extends Log {

    private int amount;
    //private boolean food;
    //private boolean corrective;
    private String type;

    public LogInsulin() {
        setAmount(0);
        //setFood(false);
        //setCorrective(false);
        setType("");
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
    /*
    public boolean isFood() {
        return food;
    }

    public void setFood(boolean food) {
        this.food = food;
    }

    public boolean isCorrective() {
        return corrective;
    }

    public void setCorrective(boolean corrective) {
        this.corrective = corrective;
    }*/

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String logType()
    {
        return "Insulin";
    }
}
