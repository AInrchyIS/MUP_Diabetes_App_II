package com.example.diabetesmanagement.service;


import com.example.diabetesmanagement.data.Log;

import java.util.ArrayList;

public interface FirestoreLogCallback {
    void onCallback(ArrayList<Log> logs, String type, long start, long end);
}
