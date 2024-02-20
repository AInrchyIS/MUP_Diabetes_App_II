package com.example.diabetesmanagement.constatntClass;

import android.content.Context;

public class SharedPreferenceClass {
    public static String DISCLAIMER_VALUE = "DISCLAIMER_VALUE";
    public static String PREFERENCES_NAME = "PREFERENCES_NAME";
    Context context;

    public SharedPreferenceClass(Context context) {
        this.context = context;
    }

    public void setDisclaimerValue(int disclaimerValue) {
        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
                .edit().putInt(DISCLAIMER_VALUE, disclaimerValue)
                .apply();
    }

    public int getDisclaimerValue() {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
                .getInt(DISCLAIMER_VALUE, 0);
    }
}
