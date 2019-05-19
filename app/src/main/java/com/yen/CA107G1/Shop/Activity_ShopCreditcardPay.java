package com.yen.CA107G1.Shop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.yen.CA107G1.Activity_Main;
import com.yen.CA107G1.R;
import com.yen.CA107G1.Server.CommonTask;
import com.yen.CA107G1.Server.ServerURL;
import com.yen.CA107G1.Util.Util;
import com.yen.CA107G1.VO.ShopOrdVO;

public class Activity_ShopCreditcardPay extends AppCompatActivity {
    private EditText card7, card8, card9, card10, card11, card12;
    private Button btnConfirmShop, btnMagicShop, btnCancelShop;
    private SharedPreferences shopSPF;
    private ShopOrdVO shopOrdVO;
    private CommonTask orderAddTask;
    private Gson gson;
    private String order;
    private String memno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopcreditcardpay);

        findViews();
    }

    public void findViews() {
        btnConfirmShop = findViewById(R.id.btnConfirmShop);
        btnCancelShop = findViewById(R.id.btnCancelShop);
        btnMagicShop = findViewById(R.id.btnMagicShop);
        card7 = findViewById(R.id.card7);
        card8 = findViewById(R.id.card8);
        card9 = findViewById(R.id.card9);
        card10 = findViewById(R.id.card10);
        card11 = findViewById(R.id.card11);
        card12 = findViewById(R.id.card12);

        shopSPF = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
        gson = new GsonBuilder().setDateFormat("MMM d,yyyy HH:mm:ss aaa").create();
        order = shopSPF.getString("shopOrder", "");
        memno = shopSPF.getString("memno", "");
        Log.e("我是我是我是我是我是我是", memno);

    }

    public void magicBtnShop(View view) {
        card7.setText("3675");
        card8.setText("9562");
        card9.setText("5310");
        card10.setText("1028");
        card11.setText("01/24");
        card12.setText("684");
    }

    public void ConfirmToPayShop(View view) {


        try {


            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "add");
            jsonObject.addProperty("mem_no", memno);
            jsonObject.addProperty("order", order);
            String jsonOut = jsonObject.toString();
            orderAddTask = new CommonTask(ServerURL.ShopOrdServlet_URL, jsonOut);
        } catch (NullPointerException e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

        ShopOrdVO successOrder = null;
        try {
            String result = orderAddTask.execute().get();

            successOrder = gson.fromJson(result, ShopOrdVO.class);
        } catch (Exception e) {
            Log.e("我是shopOrderPage的", e.toString());
        }

        if (successOrder == null) {
            Toast.makeText(this, "創建訂單失敗", Toast.LENGTH_SHORT).show();
        } else {
            Util.CART.clear();
            Bundle bundle = new Bundle();
            bundle.putSerializable("order", successOrder);
            Intent intentOrder = new Intent(this, Activity_Main.class);
            intentOrder.putExtras(bundle);
            startActivity(intentOrder);
        }
    }


    public void btnCancelShop(View view) {
        finish();
    }
}





