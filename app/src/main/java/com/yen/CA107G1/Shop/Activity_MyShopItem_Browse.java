package com.yen.CA107G1.Shop;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yen.CA107G1.R;
import com.yen.CA107G1.Util.Util;
import com.yen.CA107G1.VO.ShopItemVO;
import com.yen.CA107G1.Server.CallServletItem;
import com.yen.CA107G1.Server.ServerURL;
import com.yen.CA107G1.Server.ShopItemImgTask;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Activity_MyShopItem_Browse extends AppCompatActivity {
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private ImageView sort;
    private ShopItemImgTask shopItemImgTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopitem_browse);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);


        sort = findViewById(R.id.sort);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (staggeredGridLayoutManager.getSpanCount()) {
                    case 1:
                        staggeredGridLayoutManager.setSpanCount(2);
                        break;
                    case 2:
                        staggeredGridLayoutManager.setSpanCount(1);
                        break;
                }
            }
        });

        Gson gson = new Gson();
        Type listType = new TypeToken<List<ShopItemVO>>() {
        }.getType();

        CallServletItem callServlet = new CallServletItem();
        try {
            List<ShopItemVO> shopItemVO = gson.fromJson(callServlet.execute(ServerURL.Shop_URL).get(), listType);
            Log.e("121321321231321321", shopItemVO.toString());
            recyclerView.setAdapter(new ShopItemBrowseAdapter(this, shopItemVO));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


    private class ShopItemBrowseAdapter extends RecyclerView.Adapter<ShopItemBrowseAdapter.ViewHolder> {
        private List<ShopItemVO> teamList;
        private int imageSize;
        private Context context;
        private LayoutInflater inflater;

        private ShopItemBrowseAdapter(Context context, List<ShopItemVO> teamList) {
            this.teamList = teamList;
            this.context = context;
            inflater = LayoutInflater.from(context);
            imageSize = getResources().getDisplayMetrics().widthPixels / 4;
        }

        //建立ViewHolder，藉由ViewHolder做元件綁定
        class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView ivLogo;
            private TextView tvName;
            private ImageView ivBtn;
            private int a;
            private CardView cardView;
            private TextView itemPrice;
            private Button btnAddCart;

            private ViewHolder(final View view) {
                super(view);
                ivLogo = view.findViewById(R.id.ivLogo);
                tvName = view.findViewById(R.id.tvName);
                ivBtn = view.findViewById(R.id.ivBtn);
                itemPrice = view.findViewById(R.id.itemPrice);
                cardView = view.findViewById(R.id.cardView);
                btnAddCart = view.findViewById(R.id.btnAddCart);
                a = 0;

            }
        }


        @Override
        public int getItemCount() {
            return teamList.size();
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.card_shopitem, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            //將資料注入到View裡
            final ShopItemVO team = teamList.get(position);
            String item_no = team.getS_item_no();
            holder.tvName.setText(team.getS_item_text());
            holder.itemPrice.setText("$ " + team.getS_item_price());
            shopItemImgTask = new ShopItemImgTask(ServerURL.ShopItemCover_URL, item_no, imageSize, holder.ivLogo);
            shopItemImgTask.execute();

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(Activity_MyShopItem_Browse.this, team.getS_item_text(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("ShopItemVO", team);
                    intent.putExtras(bundle);
                    intent.setClass(view.getContext(), Activity_MyShopItem_Detail.class);
                    startActivity(intent);


                }
            });
            holder.ivBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    switch (holder.a) {
                        case 0:
                            holder.ivBtn.setImageDrawable(getResources().getDrawable(R.drawable.like));
                            Toast.makeText(v.getContext(), "已加入收藏", Toast.LENGTH_SHORT).show();
                            holder.a = 1;
                            break;
                        case 1:
                            holder.ivBtn.setImageDrawable(getResources().getDrawable(R.drawable.unlike));
                            Toast.makeText(v.getContext(), "已取消收藏", Toast.LENGTH_SHORT).show();
                            holder.a = 0;
                            break;
                    }
                }
            });
            holder.btnAddCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
ShopItemVO shopItem =new ShopItemVO();


                }
            });

        }


    }


}
