package com.example.bytebills.controller;

import android.content.Context;

import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.json.simple.JSONObject;

public class RegisterWorker extends Worker {

    public RegisterWorker(Context context, WorkerParameters workerParameters) {
        super(context, workerParameters);
    }
    public Result doWork() {
        Data data = getInputData();
        RemoteDBHandler dbHandler = new RemoteDBHandler();

        String username = data.getString("username");
        String email = data.getString("email");
        String password = data.getString("password");
        String fcm_token = data.getString("fcm_token");

        try {
            JSONObject json = new JSONObject();
            json.put("username", username);
            json.put("email", email);
            json.put("password", password);
            json.put("fcm_token", fcm_token);

            String status = dbHandler.post("register", json);

            Data outputData = new Data.Builder()
                    .putString("status", status)
                    .build();
            return Result.success(outputData);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure();
        }
    }
}
