package com.example.diabetesmanagement.data;


import com.example.diabetesmanagement.service.UserStorage;
import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

public class Goal implements Serializable {

    public final static String ACTIVITY = "Exercise";
    public final static String BLOODSUGAR = "Blood sugar logs";
    public final static String INSULIN = "Insulin logs";
    public final static String MEDICINE = "Medicine logs";
    public final static String FOODSNACK = "Snacks";
    public final static String FOODMEAL = "Meals";
    public final static String WEIGHT = "Weight logs";
    public final static String MOOD = "Mood logs";

    // type hardcoded to preset types: Activity progress, and log entry milestones of each type
    private String type;
    private String userID;
    private int dailyAmount;
    private int mealAmount;
    private int snackAmount;
    private int mealsEaten;
    private int snacksEaten;
    private int weeklyAmount;
    private int dailyProgress;
    private int weeklyProgress;
    private int timesDailyMet;
    private int timesWeeklyMet;
    private int daysActive;
    private int weeksActive;
    private Date nextDailyReset;
    private Date nextWeeklyReset;
    private boolean isVisible;
    private boolean isActive;


    public Goal(){
        setType("");
        setMealAmount(0);
        setSnackAmount(0);
        setMealsEaten(0);
        setSnacksEaten(0);
        setDailyAmount(0);
        setWeeklyAmount(0);
        setDailyProgress(0);
        setWeeklyProgress(0);
        setDaysActive(0);
        setWeeksActive(0);
        setTimesDailyMet(0);
        setTimesWeeklyMet(0);
        setVisible(false);
        setActive(true);
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMealAmount() {
        return mealAmount;
    }

    public void setMealAmount(int mealAmount) {
        this.mealAmount = mealAmount;
    }

    public int getSnackAmount() {
        return snackAmount;
    }

    public void setSnackAmount(int snackAmount) {
        this.snackAmount = snackAmount;
    }

    public int getMealsEaten() {
        return mealsEaten;
    }

    public void setMealsEaten(int mealsEaten) {
        this.mealsEaten = mealsEaten;
    }

    public int getSnacksEaten() {
        return snacksEaten;
    }

    public void setSnacksEaten(int snacksEaten) {
        this.snacksEaten = snacksEaten;
    }

    public int getDailyAmount() {
        return dailyAmount;
    }

    public void setDailyAmount(int dailyAmount) {
        this.dailyAmount = dailyAmount;
        toggleActive();
    }

    public int getWeeklyAmount() {
        return weeklyAmount;
    }

    public void setWeeklyAmount(int weeklyAmount) {
        this.weeklyAmount = weeklyAmount;
        toggleActive();
    }

    public int getDailyProgress() {
        return dailyProgress;
    }

    public void setDailyProgress(int dailyProgress) {
        this.dailyProgress = dailyProgress;
    }

    public void addDailyProgress(int progress) {
        setDailyProgress(getDailyProgress() + progress);}

    public int getWeeklyProgress() {
        return weeklyProgress;
    }

    public void setWeeklyProgress(int weeklyProgress) {
        this.weeklyProgress = weeklyProgress;
    }

    public void addWeeklyProgress(int progress) {
        setWeeklyProgress(getWeeklyProgress() + progress);}

    public int getTimesDailyMet() {
        return timesDailyMet;
    }

    public void setTimesDailyMet(int timesDailyMet) {
        this.timesDailyMet = timesDailyMet;
    }

    public int getTimesWeeklyMet() {
        return timesWeeklyMet;
    }

    public void setTimesWeeklyMet(int timesWeeklyMet) {
        this.timesWeeklyMet = timesWeeklyMet;
    }

    public int getDaysActive() {
        return daysActive;
    }

    public void setDaysActive(int daysActive) {
        this.daysActive = daysActive;
    }

    public int getWeeksActive() {
        return weeksActive;
    }

    public void setWeeksActive(int weeksActive) {
        this.weeksActive = weeksActive;
    }

    public Date getNextDailyReset() {
        return nextDailyReset;
    }

    public void setNextDailyReset(Date nextDailyReset) {
        this.nextDailyReset = nextDailyReset;
    }

    public Date getNextWeeklyReset() {
        return nextWeeklyReset;
    }

    public void setNextWeeklyReset(Date nextWeeklyReset) {
        this.nextWeeklyReset = nextWeeklyReset;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }


    public boolean resetDailyYet(Date date)
    {
        return (date.getTime() >= getNextDailyReset().getTime());
    }

    public boolean resetWeeklyYet(Date date)
    {
        return (date.getTime() >= getNextWeeklyReset().getTime());
    }

    public void resetDaily()
    {
        Calendar c = Calendar.getInstance();
        c.setTime(getNextDailyReset());
        do {
            c.add(Calendar.DATE, 1);
        } while (c.getTimeInMillis() < Calendar.getInstance().getTimeInMillis());
        setNextDailyReset(c.getTime());
        if (isActive()) {
            if (dailyGoalMet()) {
                setTimesDailyMet(timesDailyMet + 1);
            }
            setDaysActive(daysActive + 1);

        }
        UserStorage.storeGoalProgress(this);
        setDailyProgress(0);
        setMealsEaten(0);
        setSnacksEaten(0);
    }


    public void resetWeekly()
    {
        Calendar c = Calendar.getInstance();
        c.setTime(getNextWeeklyReset());
        do {
            c.add(Calendar.DATE, 7);
        } while (c.getTimeInMillis() < Calendar.getInstance().getTimeInMillis());
        setNextWeeklyReset(c.getTime());
        if (isActive()) {
            if (weeklyGoalMet())
                setTimesWeeklyMet(timesWeeklyMet + 1);
            setWeeksActive(weeksActive + 1);
        }
        setWeeklyProgress(0);
    }




    public boolean dailyGoalMet()
    {
        if (type.equals(FOODMEAL))
            return getMealAmount() <= getMealsEaten();
        else if (type.equals(FOODSNACK))
            return getSnackAmount() <= getSnacksEaten();
        else
            return getDailyProgress() >= getDailyAmount();
    }



    public boolean weeklyGoalMet()
    {
        return getWeeklyProgress() >= getWeeklyAmount();
    }



    private void toggleActive()
    {
        if (weeklyAmount == 0 && dailyAmount ==0 && !type.equals(FOODMEAL) && !type.equals(FOODSNACK)){
            setDaysActive(0);
            setWeeksActive(0);
            setActive(false);
        }
        else {
            setActive(true);
        }
    }
}
