package com.example.bytebills.controller;

import android.content.Context;

import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.bytebills.MainActivity;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class StocksUserTransactionsWorker extends Worker {

    private String TAG = "StocksUserWorker";
    public StocksUserTransactionsWorker(Context context, WorkerParameters workerParameters) {
        super(context, workerParameters);
    }
    public Result doWork() {
        Data data = getInputData();

        String symbol = data.getString("symbol");

        String usernameMA = MainActivity.username;

        try {
            String returnValue = RemoteDBHandler.get("user-transactions/" + usernameMA + "&" + symbol);
            List<String> strings = new ArrayList<String>();

            String[] dataArray = strings.toArray(new String[0]);

            Data outputData = new Data.Builder()
                    .putString("transactions", returnValue)
                    .build();

            return Result.success(outputData);


        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure();
        }
    }
}
