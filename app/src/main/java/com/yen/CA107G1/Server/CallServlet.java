package com.yen.CA107G1.Server;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CallServlet extends AsyncTask<String, Void, String> {


    private final static String TAG = "MainActivity";

    @Override
    public String doInBackground(String... strings) {
        HttpURLConnection con = null;
        StringBuilder inStr = new StringBuilder();

        try {
            con = (HttpURLConnection) new URL(strings[0]).openConnection();
            con.setDoInput(true); // allow inputs
            con.setUseCaches(false); // do not use a cached copy
            con.setRequestMethod("POST");

            String data;
            Log.e(TAG, "input: " + strings[1]);
            data = strings[1];
            OutputStream out = con.getOutputStream();
            out.write(data.getBytes());
            out.flush();

            int responseCode = con.getResponseCode();
            if (responseCode == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line;
                while ((line = br.readLine()) != null) {
                    inStr.append(line);
                }
            } else {
                Log.d(TAG, "response code: " + responseCode);
            }
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }

        Log.e(TAG, "request" + inStr.toString());
        return inStr.toString();
    }
}



