package com.yen.CA107G1.Shop;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.yen.CA107G1.HotelRoom.Activity_HotelOrderPage;
import com.yen.CA107G1.HotelRoom.Activity_HotelRroomType_Detail;
import com.yen.CA107G1.R;
import com.yen.CA107G1.Server.RoomTypeImageTask;
import com.yen.CA107G1.VO.HotelRoomTypeVO;
import com.yen.CA107G1.VO.ShopItemVO;
import com.yen.CA107G1.Server.CommonTask;
import com.yen.CA107G1.Server.ServerURL;
import com.yen.CA107G1.Server.ShopItemImgTask;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Activity_MyShopItem_Detail extends AppCompatActivity {
    TextView shopItemTitle, shopItemContent;
    ShopItemImgTask shopItemImgTask;
    String item_no;
    ShopItemVO shopItemVO;
    Bitmap bitmap;
    ImageView shopItemDetail;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopitem_detail);

        findViews();

        Intent intent = getIntent();
        shopItemVO = (ShopItemVO) intent.getExtras().getSerializable("ShopItemVO");
        item_no = shopItemVO.getS_item_no();
        if (shopItemVO == null) {
            Toast.makeText(this, "房型詳情無法顯示", Toast.LENGTH_SHORT);
        } else {
            showDetail(shopItemVO);
        }

    }

    public void findViews() {
        shopItemDetail = findViewById(R.id.shopItemDetail);
        shopItemTitle = findViewById(R.id.shopItemTitle);
        shopItemContent = findViewById(R.id.shopItemContent);
    }

    public void showDetail(final ShopItemVO shopItemVO) {
        String rtno = shopItemVO.getS_item_no();
        final int imageSize = getResources().getDisplayMetrics().widthPixels / 2;


        try {
            shopItemImgTask = new ShopItemImgTask(ServerURL.ShopItemCover_URL, rtno, imageSize);
            bitmap = shopItemImgTask.execute().get();

        } catch (Exception e) {
            Log.e("ShowDetail", e.toString());
        }

        if (bitmap != null) {

            shopItemDetail.setImageBitmap(bitmap);

        } else {
            shopItemDetail.setImageResource(R.drawable.ic_petdefault);
        }

        shopItemTitle.setText(shopItemVO.getS_item_text());
        shopItemContent.setText(shopItemVO.getS_item_describe());

    }

}
