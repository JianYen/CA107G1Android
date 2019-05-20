package com.yen.CA107G1.Shop;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yen.CA107G1.R;
import com.yen.CA107G1.Util.Contents;
import com.yen.CA107G1.VO.ShopOrdDataVO;
import com.yen.CA107G1.VO.ShopOrdVO;

import java.util.List;

public class Activity_CheckShopOrder extends AppCompatActivity {
    private RecyclerView goodsOrderRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_shop_order);

        findViews();
    }

    public void findViews() {
        goodsOrderRecyclerView = findViewById(R.id.goodsOrderRecyclerView);
    }


    private class CheckShopOrderAdapter extends RecyclerView.Adapter<Activity_CheckShopOrder.CheckShopOrderAdapter.ViewHolder> {
        private List<ShopOrdVO> shopOrdVOList;
        private int imageSize;
        private Context context;
        private LayoutInflater inflater;

        private CheckShopOrderAdapter(Context context, List<ShopOrdVO> shopOrdVOList) {
            this.shopOrdVOList = shopOrdVOList;
            this.context = context;
            inflater = LayoutInflater.from(context);
            imageSize = context.getResources().getDisplayMetrics().widthPixels / 4;

        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView tvOrderDate, tvOrderPrice, tvOrderNumber;
            private RecyclerView rvOrderDetail;

            public ViewHolder(@NonNull View view) {
                super(view);
                tvOrderNumber = view.findViewById(R.id.tvOrderNumber);
                tvOrderPrice = view.findViewById(R.id.tvOrderPrice);
                tvOrderDate = view.findViewById(R.id.tvOrderDate);
                rvOrderDetail = view.findViewById(R.id.rvOrderDetail);
            }
        }

        @NonNull
        @Override
        public Activity_CheckShopOrder.CheckShopOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_goods_order, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final ShopOrdVO shopOrdVO = shopOrdVOList.get(position);
            holder.tvOrderNumber.setText(shopOrdVO.getS_ord_no());
            holder.tvOrderPrice.setText(shopOrdVO.getS_ord_total());
            holder.tvOrderDate.setText(shopOrdVO.getOrder_time().toString());

            //一開始強制關閉CardView中的Adapter
            if (holder.rvOrderDetail.getVisibility() != View.GONE) {
                holder.rvOrderDetail.setVisibility(View.GONE);
            }
            holder.rvOrderDetail.setLayoutManager(new LinearLayoutManager(context));

        }

        @Override
        public int getItemCount() {
            return shopOrdVOList.size();
        }
    }
}
