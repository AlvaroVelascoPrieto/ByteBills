package com.example.bytebills.controller;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.StringTokenizer;

public class RemoteDBHandler {

    private static String remoteServerDirection = "http://85.58.82.92:5000/";
    //Esta es mi IP de casa y esta hosteado en mi servidor porque me era mucho mas sencillo
    //que en el puto google cloud ese pocho para hacer cambios.
    //Ya lo cambiaremos si eso.
    private static String TAG = "RemoteDBHandler";

    public RemoteDBHandler() {
    }

    public String post(String endpoint, @NonNull JSONObject json) {
        remoteServerDirection = "http://85.58.82.92:5000/";
        remoteServerDirection += endpoint;
        HttpURLConnection conn = null;

        try {
            conn = (HttpURLConnection) new URL(remoteServerDirection).openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");

            OutputStream out = conn.getOutputStream();
            System.out.println(json.toString());
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

    public static String get(String endpoint) throws ParseException {
        remoteServerDirection = "http://85.58.82.92:5000/";
        remoteServerDirection += endpoint;
        Log.d(TAG, remoteServerDirection);
        HttpURLConnection conn = null;

        try {
            String charset = "UTF-8";
            conn = (HttpURLConnection) new URL(remoteServerDirection).openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
            conn.setRequestProperty("Accept", "application/json");



            int status = conn.getResponseCode();
            String message = conn.getResponseMessage();
            Log.d(TAG, "Response " + endpoint + ": " + status + " " + message);
            Log.d(TAG, "Body: " + conn.getContent());

            if (status == 200) {
                BufferedInputStream inputStream = new BufferedInputStream(conn.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

                String line;
                StringBuilder result = new StringBuilder();

                while ((line = bufferedReader.readLine()) != null) {
                    Log.d(TAG, line);
                    result.append(line);
                }
                inputStream.close();

                return result.toString();
            }

        } catch (ProtocolException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }  catch (IOException e) {
            throw new RuntimeException(e);
        }

        JSONParser parser = new JSONParser();
        return "Error";
    }

    public static String get_symbols(@NonNull JSONObject json) throws ParseException {
        remoteServerDirection = "http://85.58.82.92:5000/";
        json.get("symbol_type");
        remoteServerDirection += json.get("symbol_type");
        System.out.println(remoteServerDirection);
        HttpURLConnection conn = null;
        JSONObject responseJSON ;

        try {
            String charset = "UTF-8";
            conn = (HttpURLConnection) new URL(remoteServerDirection).openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
            conn.setRequestProperty("Accept", "application/json");



            int status = conn.getResponseCode();
            String message = conn.getResponseMessage();
            Log.d(TAG, "Response " + json.get("symbol_type") + ": " + status + " " + message);
            Log.d(TAG, "Body: " + conn.getContent());

            if (status == 200) {
                BufferedInputStream inputStream = new BufferedInputStream(conn.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

                String line;
                StringBuilder result = new StringBuilder();

                while ((line = bufferedReader.readLine()) != null) {
                    Log.d(TAG, line);
                    result.append(line);
                }
                inputStream.close();

                return result.substring(1, result.length() - 1);
            }

        } catch (ProtocolException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }  catch (IOException e) {
            throw new RuntimeException(e);
        }

        JSONParser parser = new JSONParser();
        return "Error";
    }

    public static String delete(String endpoint, @NonNull JSONObject json) {
        remoteServerDirection += endpoint;
        Log.d(TAG, "DEELETE " + remoteServerDirection);
        HttpURLConnection conn = null;

        try {
            conn = (HttpURLConnection) new URL(remoteServerDirection).openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("DELETE");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");

            OutputStream out = conn.getOutputStream();
            Log.d(TAG, json.toString());
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
