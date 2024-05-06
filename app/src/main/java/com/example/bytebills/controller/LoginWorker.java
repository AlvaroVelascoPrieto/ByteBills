package com.example.bytebills.controller;

import android.content.Context;

import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.json.simple.JSONObject;

public class LoginWorker extends Worker {

    public LoginWorker(Context context, WorkerParameters workerParameters) {
        super(context, workerParameters);
    }
    public Result doWork() {
        Data data = getInputData();
        RemoteDBHandler dbHandler = new RemoteDBHandler();

        String username = data.getString("username");
        String email = data.getString("email");
        String password = data.getString("password");

        try {
            JSONObject json = new JSONObject();
            json.put("email", email);
            json.put("password", password);

            String status = dbHandler.post("login", json);

            if (!status.equals("Error")) {
                Data outputData = new Data.Builder()
                        .putString("status", status)
                        .build();
                return ListenableWorker.Result.success(outputData);
            } else {
                return ListenableWorker.Result.failure();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure();
        }
    }
}
