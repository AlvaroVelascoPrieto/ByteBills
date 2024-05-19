package com.example.bytebills.controller;

import android.content.Context;
import android.util.Log;

import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.bytebills.MainActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserProfileWorker extends Worker {

    private final String TAG = "UserProfileWorker";
    public UserProfileWorker(Context context, WorkerParameters workerParameters) {
        super(context, workerParameters);
    }
    public Result doWork() {
        Data data = getInputData();

        String usernameMA = MainActivity.username;

        try {
            String returnValue = RemoteDBHandler.get("profile/" + usernameMA);

            JSONObject jsonObject = new JSONObject(returnValue);

            String username = jsonObject.getString("username");
            String email = jsonObject.getString("email");

            int assetCount = jsonObject.getInt("asset_count");
            String avgValue = jsonObject.getString("avg_value");
            String totalValue = jsonObject.getString("total_value");

            JSONArray mostCommonAssetArray = jsonObject.getJSONArray("most_common_asset");
            String mostCommonAssetName = mostCommonAssetArray.getString(0);
            JSONObject mostCommonAssetDetails = mostCommonAssetArray.getJSONObject(1);
            int mostCommonAssetQty = mostCommonAssetDetails.getInt("qty");
            String mostCommonAssetValue = mostCommonAssetDetails.getString("value");

            JSONArray top3AssetsArray = jsonObject.getJSONArray("top_3_assets");
            List<String> top3Assets = new ArrayList<>();
            for (int i = 0; i < top3AssetsArray.length(); i++) {
                JSONArray assetArray = top3AssetsArray.getJSONArray(i);
                String assetName = assetArray.getString(0);
                JSONObject assetDetails = assetArray.getJSONObject(1);
                int assetQty = assetDetails.getInt("qty");
                String assetValue = assetDetails.getString("value");

                top3Assets.add(assetName + ": qty = " + assetQty + ", value = " + assetValue);
            }

            Data outputData = new Data.Builder()
                    .putString("username", username)
                    .putString("email", email)
                    .putInt("asset_count", assetCount)
                    .putString("avg_value", avgValue)
                    .putString("total_value", totalValue)
                    .putString("most_common_asset", mostCommonAssetName + ": qty = " + mostCommonAssetQty + ", value = " + mostCommonAssetValue)
                    .putStringArray("top_3_assets", top3Assets.toArray(new String[0]))
                    .build();

            return Result.success(outputData);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure();
        }
    }
}