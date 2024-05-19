package com.example.bytebills.controller;

import android.content.Context;
import android.util.Log;

import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class StockOverallInfoWorker extends Worker {

    private String TAG = "StockOverallInfoWorker";

    public StockOverallInfoWorker(Context context, WorkerParameters workerParameters) {
        super(context, workerParameters);
    }
    public Result doWork() {
        Data data = getInputData();

        String stock = data.getString("stock_id");

        try {

            String returnValue = RemoteDBHandler.get("stockOverall/" + stock);
            returnValue = returnValue.substring(1, returnValue.length() - 1).replace("\\", "");
            Log.d(TAG, returnValue);

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
