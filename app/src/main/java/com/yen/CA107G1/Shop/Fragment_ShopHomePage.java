package com.yen.CA107G1.Shop;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import com.yen.CA107G1.Server.CallServletItem;
import com.yen.CA107G1.Server.ServerURL;
import com.yen.CA107G1.Server.ShopItemImgTask;
import com.yen.CA107G1.Util.Util;
import com.yen.CA107G1.VO.CartVO;
import com.yen.CA107G1.VO.ShopItemVO;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Fragment_ShopHomePage extends Fragment {
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private ImageView checkCart;
    private ShopItemImgTask shopItemImgTask;
    private RecyclerView recyclerView;
    private StringBuilder sb;
    private String message;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_shopitem_browse, container, false);

        checkCart = view.findViewById(R.id.checkCart);

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        checkCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Activity_Cart.class);
                startActivity(intent);
            }
        });

        Gson gson = new Gson();
        Type listType = new TypeToken<List<ShopItemVO>>() {
        }.getType();

        CallServletItem callServlet = new CallServletItem();
        try {
            List<ShopItemVO> shopItemVO = gson.fromJson(callServlet.execute(ServerURL.Shop_URL).get(), listType);
            recyclerView.setAdapter(new Fragment_ShopHomePage.ShopItemBrowseAdapter(getActivity(), shopItemVO));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return view;
    }


    private class ShopItemBrowseAdapter extends RecyclerView.Adapter<Fragment_ShopHomePage.ShopItemBrowseAdapter.ViewHolder> {
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
            private ImageView btnAddCart;

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
        public Fragment_ShopHomePage.ShopItemBrowseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.card_shopitem, parent, false);
            return new Fragment_ShopHomePage.ShopItemBrowseAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final Fragment_ShopHomePage.ShopItemBrowseAdapter.ViewHolder holder, final int position) {

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
                    Toast.makeText(getActivity(), team.getS_item_text(), Toast.LENGTH_SHORT).show();
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
                            holder.ivBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_like));
                            Toast.makeText(v.getContext(), "已加入收藏", Toast.LENGTH_SHORT).show();
                            holder.a = 1;
                            break;
                        case 1:
                            holder.ivBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_unlike));
                            Toast.makeText(v.getContext(), "已取消收藏", Toast.LENGTH_SHORT).show();
                            holder.a = 0;
                            break;
                    }
                }
            });
            holder.btnAddCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    new AlertDialog.Builder(context)
                            .setIcon(R.drawable.ic_cart)
                            .setTitle("確定要加入購物車嗎")
                            .setMessage(message)
                            .setPositiveButton("確定",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            CartVO cartVO = new CartVO(team, 1);
                                            int index = Util.CART.indexOf(cartVO);
                                            if (index == -1) {
                                                Util.CART.add(cartVO);
                                            } else {
                                                cartVO = Util.CART.get(index);
                                                cartVO.setQuantity(cartVO.getQuantity() + 1);
                                            }
                                            sb = new StringBuilder();
                                            for (CartVO cartProduct : Util.CART) {
                                                String text = "\n" + cartProduct.getS_item_text() + " x "
                                                        + cartProduct.getQuantity();
                                                sb.append(text);

                                            }
                                            message = "目前購物車有" + "\n " + sb.toString() + "\n";
                                        }
                                    })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    }
                            ).show();
                }
            });
        }


    }


}
