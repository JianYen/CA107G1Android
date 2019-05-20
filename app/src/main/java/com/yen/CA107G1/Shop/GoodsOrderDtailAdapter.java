package com.yen.CA107G1.Shop;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yen.CA107G1.R;
import com.yen.CA107G1.VO.ShopOrdDataVO;

import java.util.List;

public class GoodsOrderDtailAdapter extends RecyclerView.Adapter<GoodsOrderDtailAdapter.ViewHolder> {
    private int imageSize;
    private Context context;
    private LayoutInflater inflater;
    public List<ShopOrdDataVO> shopOrdDataVOList = null;

    private GoodsOrderDtailAdapter(Context context, List<ShopOrdDataVO> shopOrdDataVOList) {
        this.shopOrdDataVOList = shopOrdDataVOList;
        this.context = context;
        inflater = LayoutInflater.from(context);
        imageSize = context.getResources().getDisplayMetrics().widthPixels / 4;

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivGoodPic;
        private TextView tvGoodName, tvGoodAmount, tvGoodPrice, tvGoodTotal;


        public ViewHolder(@NonNull View view) {
            super(view);
            ivGoodPic = view.findViewById(R.id.ivGoodPic);
            tvGoodName = view.findViewById(R.id.tvGoodName);
            tvGoodAmount = view.findViewById(R.id.tvGoodAmount);
            tvGoodPrice = view.findViewById(R.id.tvGoodPrice);
            tvGoodTotal = view.findViewById(R.id.tvGoodTotal);
        }
    }

    @NonNull
    @Override
    public GoodsOrderDtailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_my_good_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ShopOrdDataVO shopOrdDataVO = shopOrdDataVOList.get(position);
        holder.tvGoodName.setText(shopOrdDataVO.getS_item_no());
        holder.tvGoodPrice.setText(String.valueOf(shopOrdDataVO.getS_ord_price()));
        holder.tvGoodAmount.setText(String.valueOf(shopOrdDataVO.getS_ord_count()));
        holder.tvGoodTotal.setText(String.valueOf(shopOrdDataVO.getS_ord_count() * shopOrdDataVO.getS_ord_price()));


    }

    @Override
    public int getItemCount() {
        return shopOrdDataVOList.size();
    }
}
