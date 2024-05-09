package com.example.bytebills.controller;

import android.content.Context;

import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.json.simple.JSONObject;

public class StockInfoWorker extends Worker {

    public StockInfoWorker(Context context, WorkerParameters workerParameters) {
        super(context, workerParameters);
    }
    public Result doWork() {
        Data data = getInputData();

        String stock = data.getString("stock_id");

        try {
            JSONObject json = new JSONObject();
            json.put("stock", stock);

            JSONObject returnValue = RemoteDBHandler.get("stock", json);

            Data outputData = new Data.Builder()
                    .putString("value", String.valueOf(returnValue))
                    .build();
            return Result.success(outputData);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure();
        }
    }
}
