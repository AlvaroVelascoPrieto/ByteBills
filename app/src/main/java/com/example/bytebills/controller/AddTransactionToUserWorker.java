package com.example.bytebills.controller;

import android.content.Context;

import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.json.simple.JSONObject;

public class AddTransactionToUserWorker extends Worker {

    public AddTransactionToUserWorker(Context context, WorkerParameters workerParameters) {
        super(context, workerParameters);
    }
    public ListenableWorker.Result doWork() {
        Data data = getInputData();
        RemoteDBHandler dbHandler = new RemoteDBHandler();

        String username = data.getString("username");
        String symbol = data.getString("symbol");
        String price = data.getString("price");
        String qty = data.getString("quantity");
        String time = data.getString("timestamp");

        try {
            JSONObject json = new JSONObject();
            json.put("username", username);
            json.put("symbol", symbol);
            json.put("price", price);
            json.put("quantity", qty);
            json.put("buy_timestamp", time);

            String status = dbHandler.post("add-transaction-to-user", json);

            Data outputData = new Data.Builder()
                    .putString("status", status)
                    .build();
            return ListenableWorker.Result.success(outputData);

        } catch (Exception e) {
            e.printStackTrace();
            return ListenableWorker.Result.failure();
        }
    }
}


