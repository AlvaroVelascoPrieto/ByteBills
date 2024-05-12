package com.example.bytebills.controller;

import android.content.Context;
import android.util.Log;

import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.bytebills.MainActivity;

import org.json.simple.JSONObject;

public class StocksUserWorker extends Worker {

    private String TAG = "StocksUserWorker";
    public StocksUserWorker(Context context, WorkerParameters workerParameters) {
        super(context, workerParameters);
    }
    public Result doWork() {
        Data data = getInputData();

        String usernameMA = MainActivity.username;
        Log.d(TAG, "MainActivity.username in StocksUserWorker -> " + usernameMA);
        String username = data.getString("username");
        Log.d(TAG, "StocksUserWorker username -> " + username);

        try {
            String returnValue = RemoteDBHandler.get("user-stocks/" + usernameMA);

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
