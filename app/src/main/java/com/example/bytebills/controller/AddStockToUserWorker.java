package com.example.bytebills.controller;

import android.content.Context;

import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.bytebills.ui.addStock.AddStockFragment;

import org.json.simple.JSONObject;

public class AddStockToUserWorker extends Worker {

    public AddStockToUserWorker(Context context, WorkerParameters workerParameters) {
        super(context, workerParameters);
    }
    public ListenableWorker.Result doWork() {
        Data data = getInputData();
        RemoteDBHandler dbHandler = new RemoteDBHandler();

        String username = data.getString("username");
        String symbol = data.getString("symbol");

        try {
            JSONObject json = new JSONObject();
            json.put("username", username);
            json.put("symbol", symbol);

            String status = dbHandler.post("add-stock-to-user", json);

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

