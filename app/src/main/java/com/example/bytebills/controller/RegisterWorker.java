package com.example.bytebills.controller;

import android.content.Context;

import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.json.JSONException;
import org.json.JSONObject;

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

        try {
            JSONObject json = new JSONObject();
            json.put("username", username);
            json.put("email", email);
            json.put("password", password);

            String status = dbHandler.post("register", json);

            if (!status.equals("Error")) {
                Data outputData = new Data.Builder()
                        .putString("status", status)
                        .build();
                return Result.success(outputData);
            } else {
                return Result.failure();
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
