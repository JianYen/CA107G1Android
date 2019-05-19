package com.yen.CA107G1.Shop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yen.CA107G1.R;
import com.yen.CA107G1.Util.Util;
import com.yen.CA107G1.VO.ShopOrdVO;

public class Activity_ShopOrderPage extends AppCompatActivity {
    private EditText shopInfoET1, shopInfoET2, shopInfoET3;
    private Button btnShopInfoMagic , btnShopInfoConfirm;
    private SharedPreferences spf;
    private String order;
    private Gson gson;
    private ShopOrdVO shopOrdVO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_order_page);

        findViews();
    }

    public void findViews(){
        shopInfoET1 = findViewById(R.id.shopInfoET1);
        shopInfoET2 = findViewById(R.id.shopInfoET2);
        shopInfoET3 = findViewById(R.id.shopInfoET3);
        btnShopInfoMagic = findViewById(R.id.btnShopInfoMagic);
        btnShopInfoConfirm = findViewById(R.id.btnShopInfoConfirm);
        spf = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
        order = spf.getString("shopOrder","");
        gson = new GsonBuilder().setDateFormat("MMM d,yyyy HH:mm:ss aaa").create();
        shopOrdVO = gson.fromJson(order, ShopOrdVO.class);

    }

    public void shopMagic(View view) {
        shopInfoET1.setText("美國隊長");
        shopInfoET2.setText("0912456789");
        shopInfoET3.setText("桃園市中壢區中大路300號");
    }

    public void goShopPay(View view) {
        String infoName = shopInfoET1.getText().toString().trim();
        String infoPhone = shopInfoET2.getText().toString().trim();
        String infoAdd = shopInfoET3.getText().toString().trim();

        shopOrdVO.setS_ord_address(infoAdd);
        String shopOrder = gson.toJson(shopOrdVO);
        spf.edit().putString("shopOrder",shopOrder).apply();
        Intent intent =new Intent(Activity_ShopOrderPage.this, Activity_ShopCreditcardPay.class);
        startActivity(intent);
    }


}
