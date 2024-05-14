package com.example.bytebills.controller;

import android.content.Context;

import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.json.simple.JSONObject;

public class AddDividendToUserWorker extends Worker {

    public AddDividendToUserWorker(Context context, WorkerParameters workerParameters) {
        super(context, workerParameters);
    }
    public Result doWork() {
        Data data = getInputData();
        RemoteDBHandler dbHandler = new RemoteDBHandler();

        String username = data.getString("username");
        String symbol = data.getString("symbol");
        String price = data.getString("amount");
        String time = data.getString("timestamp");

        try {
            JSONObject json = new JSONObject();
            json.put("username", username);
            json.put("symbol", symbol);
            json.put("amount", price);
            json.put("timestamp", time);

            String status = dbHandler.post("add-dividend-to-user", json);

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


