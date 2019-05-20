package com.yen.CA107G1.Shop;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yen.CA107G1.Member.Activity_MemberLogin;
import com.yen.CA107G1.R;
import com.yen.CA107G1.Server.CommonTask;
import com.yen.CA107G1.Server.ServerURL;
import com.yen.CA107G1.Server.ShopItemImgTask;
import com.yen.CA107G1.Util.Util;
import com.yen.CA107G1.VO.CartVO;
import com.yen.CA107G1.VO.MemberVO;
import com.yen.CA107G1.VO.ShopOrdVO;

import java.util.GregorianCalendar;
import java.util.List;

public class Activity_Cart extends AppCompatActivity {
    private final static String TAG = "CartActivity";
    private final static int REQUEST_LOGIN = 0;
    private RecyclerView rvItems;
    private LinearLayout layoutEmpty;
    private TextView tvTotal;
    private CommonTask orderAddTask;
    private ShopItemImgTask shopItemImgTask;
    private Gson gson;
    private MemberVO memberVO;
    private SharedPreferences loginSPF, shopSPF, pref;
    private String member;
    String memno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        findViews();
    }

    private void findViews() {
        layoutEmpty = findViewById(R.id.layoutEmpty);
        tvTotal = findViewById(R.id.tvTotal);
        rvItems = findViewById(R.id.rvItems);
        rvItems.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        loginSPF = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
        shopSPF = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        showTotal(Util.CART);
        rvItems.setAdapter(new CartRecyclerViewAdapter(this, Util.CART));
    }

    private void showTotal(List<CartVO> cartList) {
        double total = 0;
        for (CartVO item : cartList) {
            total += item.getS_item_price() * item.getQuantity();
        }
        String text = "總金額: " + total + " 元$";
        tvTotal.setText(text);
    }

    public void onCheckoutClick(View view) {
        if (Util.CART == null || Util.CART.size() <= 0) {
            Toast.makeText(this, "購物車空空的", Toast.LENGTH_SHORT).show();
            return;
        } else if (loginSPF.getBoolean("login", false) == true) {
            pref = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
            member = pref.getString("member", "");
            gson = new GsonBuilder().setDateFormat("MMM d,yyyy HH:mm:ss aaa").create();
            memberVO = gson.fromJson(member, MemberVO.class);
            memno = memberVO.getMem_no();
            if (Util.networkConnected(this)) {


                ShopOrdVO order = new ShopOrdVO();
                order.setMem_no(memno);
                int sum = 0;
                for (CartVO item : Util.CART) {
                    sum += item.getS_item_price() * item.getQuantity();
                }
                java.sql.Timestamp orderTime = new java.sql.Timestamp(new GregorianCalendar().getTimeInMillis());
                order.setS_ord_total(sum);
                order.setCartList(Util.CART);
                order.setS_status_no(2);
                order.setS_ord_mem_points(0);
                order.setOrder_time(orderTime);

                shopSPF.edit().putString("shopOrder", gson.toJson(order)).apply();

                new AlertDialog.Builder(this)
                        .setTitle("確認購買嗎 ?")
                        .setMessage("總金額為" + sum + "元")
                        .setIcon(R.drawable.ic_paymoney)
                        .setPositiveButton("確認結帳去", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Activity_Cart.this, Activity_ShopOrderPage.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("還想再逛一下", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show();


            }


        } else {
            new AlertDialog.Builder(this)
                    .setTitle("請先登入")
                    .setMessage("您現在尚未登入～")
                    .setPositiveButton("GO,現在登入去", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent loginIntent = new Intent(Activity_Cart.this, Activity_MemberLogin.class);
                            startActivity(loginIntent);
                        }
                    })
                    .setNegativeButton("先不要", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).show();
        }
    }

    public void onEmptyCartClick(View view) {
        if (Util.CART == null || Util.CART.size() <= 0) {
            Toast.makeText(this, "購物車已經是空的!!", Toast.LENGTH_SHORT);
            return;
        }

        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_error)
                .setTitle("系統訊息")
                .setMessage("確定要清空嗎")
                .setPositiveButton("確定",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Util.CART.clear();
                                showTotal(Util.CART);
                                // notifyDataSetChanged()
                                // refresh data set
                                rvItems.getAdapter().notifyDataSetChanged();
                            }
                        })

                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.cancel();
                            }
                        }).setCancelable(false).show();
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            case REQUEST_LOGIN:
//                if (resultCode == RESULT_OK) {
//
//                }
//                break;
//
//        }
//    }

    private class CartRecyclerViewAdapter extends RecyclerView.Adapter<CartRecyclerViewAdapter.MyViewHolder> {
        private Context context;
        private LayoutInflater layoutInflater;
        private List<CartVO> orderBookList;
        private int imageSize;

        CartRecyclerViewAdapter(Context context, List<CartVO> orderBookList) {
            this.context = context;
            layoutInflater = LayoutInflater.from(context);
            this.orderBookList = orderBookList;
            imageSize = getResources().getDisplayMetrics().widthPixels / 4;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            View itemView;
            ImageView ivCartBookImage, ivCartRemoveItem;
            TextView tvCartBookName, tvCartBookPrice;
            Spinner spCartBookQuantity;

            MyViewHolder(View itemView) {
                super(itemView);
                this.itemView = itemView;
                ivCartBookImage = itemView.findViewById(R.id.ivCartBookImage);
                ivCartRemoveItem = itemView.findViewById(R.id.ivCartRemoveItem);
                tvCartBookName = itemView.findViewById(R.id.tvCartBookName);
                tvCartBookPrice = itemView.findViewById(R.id.tvCartBookPrice);
                spCartBookQuantity = itemView.findViewById(R.id.spCartBookQuantity);
            }
        }

        @Override
        public int getItemCount() {
            if (Util.CART.size() <= 0) {
                layoutEmpty.setVisibility(View.VISIBLE);
            } else {
                layoutEmpty.setVisibility(View.GONE);
            }
            return orderBookList.size();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.card_cart, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final CartVO cartVO = orderBookList.get(position);

            String item_no = cartVO.getS_item_no();
            shopItemImgTask = new ShopItemImgTask(ServerURL.ShopItemCover_URL, item_no, imageSize, holder.ivCartBookImage);
            shopItemImgTask.execute();
            holder.tvCartBookName.setText(cartVO.getS_item_text());
            holder.tvCartBookPrice.setText(String.valueOf(cartVO.getS_item_price()));
            holder.spCartBookQuantity.setSelection(cartVO.getQuantity() - 1, true);
            holder.spCartBookQuantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent,
                                           View view, int position, long id) {
                    int quantity = Integer.parseInt(parent
                            .getItemAtPosition(position).toString());
                    cartVO.setQuantity(quantity);
                    showTotal(Util.CART);
                    Util.showToast(context,
                            "已從購物車移除" + " " +
                                    cartVO.getS_item_text());
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }
            });
            holder.ivCartRemoveItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String message = "移除" + "「"
                            + cartVO.getS_item_text() + "」?";
                    new AlertDialog.Builder(context)
                            .setIcon(R.drawable.ic_cart)
                            .setTitle("確定要移除嗎")
                            .setMessage(message)
                            .setPositiveButton("確定",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            Util.CART.remove(cartVO);
                                            showTotal(Util.CART);
                                            CartRecyclerViewAdapter.this
                                                    .notifyDataSetChanged();
                                        }
                                    })
                            .setNegativeButton("取消",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            dialog.cancel();
                                        }
                                    }).setCancelable(false).show();
                }
            });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (orderAddTask != null) {
            orderAddTask.cancel(true);
        }
        if (shopItemImgTask != null) {
            shopItemImgTask.cancel(true);
        }
    }


}
