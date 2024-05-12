package com.example.bytebills.controller;

import android.content.Context;
import android.util.Log;

import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.bytebills.MainActivity;

import org.json.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StocksUserWorker extends Worker {

    private String TAG = "StocksUserWorker";
    public StocksUserWorker(Context context, WorkerParameters workerParameters) {
        super(context, workerParameters);
    }
    public Result doWork() {
        Data data = getInputData();

        String usernameMA = MainActivity.username;

        try {
            String returnValue = RemoteDBHandler.get("user-stocks/" + usernameMA);
            List<String> strings = new ArrayList<String>();

            JSONArray jsonArray = new JSONArray(returnValue);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONArray inner = jsonArray.getJSONArray(i);
                for (int j = 0; j < inner.length(); j++) {
                    String stock = inner.getString(j);
                    strings.add(stock);
                }
            }

            String[] dataArray = strings.toArray(new String[0]);

            Data outputData = new Data.Builder()
                    .putStringArray("stocks", dataArray)
                    .build();

            return Result.success(outputData);


        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure();
        }
    }
}
