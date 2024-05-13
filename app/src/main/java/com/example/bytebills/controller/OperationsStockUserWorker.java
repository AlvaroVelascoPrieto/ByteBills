package com.example.bytebills.controller;

import android.content.Context;

import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.bytebills.MainActivity;
import com.example.bytebills.ui.stockvalue.StockValueActivity;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class OperationsStockUserWorker extends Worker {

    private String TAG = "OperationsStockUserWorker";
    public OperationsStockUserWorker(Context context, WorkerParameters workerParameters) {
        super(context, workerParameters);
    }
    public Result doWork() {
        Data data = getInputData();

        String usernameMA = MainActivity.username;
        String stock = StockValueActivity.stock_id;

        try {
            String returnValue = RemoteDBHandler.get("user-transactions-stock/" + usernameMA + "/" + stock);
            List<String> strings = new ArrayList<String>();

            JSONArray jsonArray = new JSONArray(returnValue);
            //TODO: PARSE data for purchases and dividends

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
