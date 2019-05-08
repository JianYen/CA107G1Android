package com.yen.CA107G1.HotelRoom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.yen.CA107G1.Activity_Main;
import com.yen.CA107G1.R;
import com.yen.CA107G1.Util.Util;
import com.yen.CA107G1.VO.HotelOrderVO;
import com.yen.CA107G1.Server.CommonTask;
import com.yen.CA107G1.Server.ServerURL;

import java.util.concurrent.ExecutionException;

public class Activity_CredicardPay extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private HotelOrderVO hotelOrderVO;
    private Button btnConfirm, btnMagic, btnCancel;
    private EditText card1, card2, card3, card4, card5, card6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credicard_pay);

        findViews();
    }

    public void findViews() {
        btnConfirm = findViewById(R.id.btnConfirm);
        btnCancel = findViewById(R.id.btnCancel);
        btnMagic = findViewById(R.id.btnMagic);
        card1 = findViewById(R.id.card1);
        card2 = findViewById(R.id.card2);
        card3 = findViewById(R.id.card3);
        card4 = findViewById(R.id.card4);
        card5 = findViewById(R.id.card5);
        card6 = findViewById(R.id.card6);

        sharedPreferences = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
        
    }


    public void ConfirmToPay(View view) {
        String hoVO = sharedPreferences.getString("hoVO", null);
        Gson gson = new Gson();
        hotelOrderVO = gson.fromJson(hoVO, HotelOrderVO.class);

        Gson gsonBuilder = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        String req = gsonBuilder.toJson(hotelOrderVO);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "insert");
        jsonObject.addProperty("HotelOrdVO", req);


        try {
            new CommonTask(ServerURL.HotelOrder_URL, jsonObject.toString()).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(Activity_CredicardPay.this, Activity_Main.class);
        startActivity(intent);
    }


    public void magic(View view) {
                card1.setText("3345");
                card2.setText("6789");
                card3.setText("5310");
                card4.setText("0821");
                card5.setText("01/24");
                card6.setText("693");
    }
}