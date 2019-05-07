package com.yen.shoppingcar.Shop;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.yen.shoppingcar.R;
import com.yen.shoppingcar.VO.ShopItemVO;
import com.yen.shoppingcar.myServer.CommonTask;
import com.yen.shoppingcar.myServer.ServerURL;
import com.yen.shoppingcar.myServer.ShopItemImgTask;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MyShopItem_Detail extends AppCompatActivity {
    ViewPager vpShopItem;
    TextView shopItemTitle, shopItemContent;
    ShopItemImgTask shopItemImgTask;
    List<String> shopItemImgVOList;
    private int imageSize = getResources().getDisplayMetrics().widthPixels / 2;
    String item_no;
    ShopItemVO shopItemVO;
    Bitmap bitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopitem_detail);
        vpShopItem = findViewById(R.id.vpShopItem);
        shopItemTitle = findViewById(R.id.shopItemTitle);
        shopItemContent = findViewById(R.id.shopItemContent);


        Intent intent = getIntent();
         shopItemVO = (ShopItemVO) intent.getExtras().getSerializable("ShopItemVO");

        item_no = shopItemVO.getS_item_no();


        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "findItemNo");
            jsonObject.addProperty("getItemNo", item_no);
            String itemImgList = new CommonTask(ServerURL.ShopItemImg_URL, jsonObject.toString()).execute().get();
            Type listType= new TypeToken<List<String>>(){}.getType();
            Gson gson = new Gson();
            shopItemImgVOList = gson.fromJson(itemImgList, listType);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        vpShopItem.setAdapter(new MyPagerAdapter(MyShopItem_Detail.this, shopItemImgVOList));


        shopItemTitle.setText(shopItemVO.getS_item_text());
        shopItemContent.setText(shopItemVO.getS_item_describe());


    }

    private class MyPagerAdapter extends PagerAdapter {
        private Context context;
        private List list;

        public MyPagerAdapter(Context context, List<String> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            // 佈局
            View itemView = LayoutInflater.from(container.getContext()).inflate(R.layout.card_image, container, false);

            // 佈局元件內容
            ImageView imageView = itemView.findViewById(R.id.imageForShop);

            // 加載
            (container).addView(itemView);

            return itemView;
        }

    }

}
