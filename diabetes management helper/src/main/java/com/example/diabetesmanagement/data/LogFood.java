package com.example.diabetesmanagement.data;

public class LogFood extends Log {

    //private int carbs;
    private String itemsEaten;
    private boolean meal;
    private boolean snack;

    public LogFood() {
        //setCarbs(0);
        setMeal(false);
        setSnack(false);
        setItemsEaten("");
    }
    /*
    public int getCarbs() {
        return carbs;
    }

    public void setCarbs(int carbs) {
        this.carbs = carbs;
    }
    */
    public String getItemsEaten() {
        return itemsEaten;
    }

    public void setItemsEaten(String itemsEaten) {
        this.itemsEaten = itemsEaten;
    }

    public boolean isMeal() {
        return meal;
    }

    public void setMeal(boolean meal) {
        this.meal = meal;
    }

    public boolean isSnack() {
        return snack;
    }

    public void setSnack(boolean snack) {
        this.snack = snack;
    }

    public String logType()
    {
        return "Food";
    }
}
