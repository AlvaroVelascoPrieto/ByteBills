package com.example.bytebills.controller;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class RemoteDBHandler {

    private String remoteServerDirection = "http://192.168.1.7:5000/";
    private String TAG = "RemoteDBHandler";

    public RemoteDBHandler() {
    }

    public String post(String endpoint, @NonNull JSONObject json) { remoteServerDirection += endpoint;
        HttpURLConnection conn = null;

        try {
            conn = (HttpURLConnection) new URL(remoteServerDirection).openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");

            OutputStream out = conn.getOutputStream();
            out.write(json.toString().getBytes());
            out.flush();
            out.close();

            int status = conn.getResponseCode();
            String message = conn.getResponseMessage();
            Log.d(TAG, "Response " + endpoint + ": " + status + " " + message);
            Log.d(TAG, "Body: " + conn.getContent());

            if (status == 200) {
                BufferedInputStream inputStream = new BufferedInputStream(conn.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

                String line;
                StringBuilder result = new StringBuilder();

                while ((line = bufferedReader.readLine()) != null) {
                    Log.d(TAG, line);
                    result.append(line);
                }
                inputStream.close();

                JSONParser parser = new JSONParser();
                JSONObject responseJSON = (JSONObject) parser.parse(result.toString());

                return responseJSON.get("status").toString();
            }

        } catch (ProtocolException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


        return "Error";
    }

}
