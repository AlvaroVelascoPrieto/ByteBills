package com.example.bytebills.controller;

import android.content.Context;

import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.json.simple.JSONObject;

public class SymbolnfoWorker extends Worker {

    public SymbolnfoWorker(Context context, WorkerParameters workerParameters) {
        super(context, workerParameters);
    }
    public Result doWork() {
        Data data = getInputData();

        String symbol_type = data.getString("symbol_type");

        try {
            JSONObject json = new JSONObject();
            json.put("symbol_type", symbol_type);

            String returnValue = RemoteDBHandler.get_symbols(json);

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
