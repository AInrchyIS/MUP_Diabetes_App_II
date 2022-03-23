package com.example.diabetesmanagement.constatntClass;

public class ConstantClass {
    private static String authUserId;
    public static String totalScoresFireBase="Total Scores";
    public static String counterText = "counterText";
    public static String counterIncrement = "counterIncrement";

    public static String bloodSugarImageTextBeforeMeal="bloodSugarImageTextBeforeMeal";
    public static String bloodSugarImageTextAfterMeal="bloodSugarImageTextAfterMeal";
    public static String bloodSugarImageTextBeforeBed="bloodSugarImageTextBeforeBed";
    public static String medicineImageText="medicineImageText";
    public static String insulinImageText="insulinImageText";

    public static String foodImageTextMeal="foodImageTextMeal";
    public static String foodImageTextSnack="foodImageTextSnack";
    public static String foodImageTextItemsEaten="foodImageTextItemsEaten";

    public static String activityImageText="activityImageText";
    public static String weightImageText="weightImageText";
    public static String moodImageText="moodImageText";

    public static String todayTaskText="todayTaskText";
    public static String todayTaskIncrement="todayTaskIncrement";
    public static String badgeIncrement="badgeIncrement";
    public static String badgeTextIncrement="badgeTextincrement";

    public static String getAuthUserId() {
        return authUserId;
    }

    public static void setAuthUserId(String authUserId) {
        ConstantClass.authUserId = authUserId;
    }
}
