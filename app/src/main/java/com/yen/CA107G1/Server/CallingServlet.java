package com.yen.CA107G1.Server;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class CallingServlet {
    private final static String TAG = "1212121211211212121";



    public static String getConnect(String url, String outStr) {
        HttpURLConnection con = null;
        StringBuilder inStr = new StringBuilder();
        try {
            con = (HttpURLConnection) new URL(url).openConnection();
            con.setDoInput(true); // allow inputs
            con.setDoOutput(true); // allow outputs
            // 不知道請求內容大小時可以呼叫此方法將請求內容分段傳輸，設定0代表使用預設大小
            //con.setChunkedStreamingMode(0);
            con.setUseCaches(false); // do not use a cached copy
            con.setRequestMethod("POST");
            //con.setRequestProperty("charset", "UTF-8");
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
            bw.write(outStr);
            Log.d(TAG, "output: " + outStr);
            bw.close();

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
        Log.d(TAG, "input: " + inStr);
        return inStr.toString();
    }


}


