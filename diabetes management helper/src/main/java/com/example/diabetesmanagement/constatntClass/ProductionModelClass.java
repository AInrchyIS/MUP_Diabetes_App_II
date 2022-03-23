//package com.example.diabetesmanagement.constatntClass;
//
//import android.util.Log;
//
//import java.io.Serializable;
//
//public class ProductionModelClass implements Serializable {
//
//    private static final String TAG = "123";
//    int totalScore, todayScore, numberOfBadgesEarned;
//    boolean Today, Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday;
//    public ProductionModelClass(){
//
//    }
//    public ProductionModelClass(int totalScore, int todayScore, int numberOfBadgesEarned, boolean Today) {
//        this.totalScore = totalScore;
//        this.todayScore = todayScore;
//        this.numberOfBadgesEarned = numberOfBadgesEarned;
//        this.Today = Today;
//        Log.d(TAG, "ProductionModelClass: Total Scores"+totalScore +"Today Scores"+todayScore+"Number Of Badges"+numberOfBadgesEarned);
//    }
//
//
//    public int getTotalScore() {
//        return totalScore;
//    }
//
//    public int getTodayScore() {
//        return todayScore;
//    }
//
//    public int getNumberOfBadgesEarned() {
//        return numberOfBadgesEarned;
//    }
//
//    public boolean getToday(){
//        return this.Today;
//    }
//
//    public void setTotalScore(int totalScore) {
//        this.totalScore = totalScore;
//    }
//
//    public void setTodayScore(int todayScore) {
//        this.todayScore = todayScore;
//    }
//
//    public void setNumberOfBadgesEarned(int numberOfBadgesEarned) {
//        this.numberOfBadgesEarned = numberOfBadgesEarned;
//    }
//
//    public void setSunday(boolean b){
//        this.Sunday = b;
//    }
//    public void setMonday(boolean b){
//        this.Monday = b;
//    }
//    public void setTuesday(boolean b){
//        this.Tuesday = b;
//    }
//    public void setWednesday(boolean b){this.Wednesday = b;}
//    public void setThursday(boolean b){
//        this.Thursday = b;
//    }
//    public void setFriday(boolean b){
//        this.Friday = b;
//    }
//    public void setSaturday(boolean b){
//        this.Saturday = b;
//    }
//    public void setToday(boolean b){
//        this.Today = b;
//    }
//
//
//    public boolean getSunday(){
//        return this.Sunday;
//    }
//    public boolean getMonday(){
//        return this.Monday;
//    }
//    public boolean getTuesday(){
//        return this.Tuesday;
//    }
//    public boolean getWednesday(){
//        return this.Wednesday;
//    }
//    public boolean getThursday(){
//        return this.Thursday;
//    }
//    public boolean getFriday(){
//        return this.Friday;
//    }
//    public boolean getSaturday(){
//        return this.Saturday;
//    }
//
//
//
//
//}
