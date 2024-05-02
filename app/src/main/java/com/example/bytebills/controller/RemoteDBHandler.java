package com.example.bytebills.controller;

import android.content.Context;
import android.util.Log;

import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.json.JSONException;
import org.json.JSONObject;
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

public class RemoteDBHandler extends Worker {

    private String remoteServerDirection = "http://ip:5000";
    private String TAG = "RemoteDBHandler";

    public RemoteDBHandler(Context context, WorkerParameters workerParameters) {
        super(context, workerParameters);
    }

    //Este metodo es un dispatcher, es una tarea asincrona que deriva en una interaccion con la base de datos
    //segun el valor del tag
    public Result doWork() {
        Data data = getInputData();
        String tag = data.getString("tag");

        if (tag != null) {
            switch (tag) {
                case "Register":
                    String username = data.getString("username");
                    String email = data.getString("email");
                    String password = data.getString("password");
                    return registerUser(username, email, password);
                case "Login":
                    break;
            }
        }

        return Result.failure();
    }

    private Result registerUser(String username, String password, String email) {

        Log.d(TAG, "registerUser entered");
        remoteServerDirection += "/register";

        HttpURLConnection conn = null;

        JSONObject json = new JSONObject();
        try {
            json.put("username", username);
            json.put("password", password);
            json.put("email", email);
            Log.d(TAG, "register JSON created: " + json.toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        //Este try catch es el intento de hacer una llamada HTTP al servidor remoto con los datos introducidos por el usuario
        //en forma de objeto JSON

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
            Log.d(TAG, "Response register: " + status + " " + message);

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

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonResponse = (JSONObject) jsonParser.parse(result.toString());

                if (jsonResponse.get("status").equals("Ok")) {
                    return Result.success();
                }
            }


        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return Result.failure();
    }

}
