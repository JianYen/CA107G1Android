package com.yen.shoppingcar;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yen.shoppingcar.VO.HomePageVO;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;


public class Fragment_HomePage extends Fragment {
    Banner banner;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hompage, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        List<HomePageVO> hpList = new ArrayList<>();
        hpList.add(new HomePageVO(R.drawable.icon_member, "會員資料"));
        hpList.add(new HomePageVO(R.drawable.icon_likes, "我的收藏"));
        hpList.add(new HomePageVO(R.drawable.icon_pet, "寵物資料"));
        hpList.add(new HomePageVO(R.drawable.icon_order, "我的訂單"));



        recyclerView.setAdapter(new HomePageAdapter(hpList));

        banner = view.findViewById(R.id.hPbr);
        banner.setImageLoader(new GlideImageLoader());
        List<Integer> list = new ArrayList<>();
        list.add(R.drawable.beauty);
        list.add(R.drawable.beauty2);
//        list.add("https://imgur.dcard.tw/5Ju2c2t.jpg");
//        list.add("https://cdn2.ettoday.net/images/1943/d1943865.jpg");
//        list.add("https://video.nextmag.com.tw/photo/2016/07/14/1_1468495982439_606484_ver1.0.jpg");
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.setImages(list);
        banner.isAutoPlay(true);
        banner.setDelayTime(2000);
        banner.start();
        return view;
    }

    private class HomePageAdapter extends RecyclerView.Adapter<HomePageAdapter.ViewHolder> {
        private List<HomePageVO> hpList;

        private HomePageAdapter(List<HomePageVO> hpList) {
            this.hpList = hpList;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView ivLogo;
            private TextView tvName;

            private ViewHolder(View view) {
                super(view);
                ivLogo = view.findViewById(R.id.ivLogo);
                tvName = view.findViewById(R.id.tvName);
            }
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_home_page, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final HomePageVO homePageTeam = hpList.get(position);
            holder.ivLogo.setImageResource(homePageTeam.getLogo());
            holder.tvName.setText(homePageTeam.getName());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), homePageTeam.getName(), Toast.LENGTH_SHORT).show();
                }
            });

        }


        @Override
        public int getItemCount() {
            return hpList.size();
        }
    }

    public class GlideImageLoader extends ImageLoader {


        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load((Integer) path).into(imageView);

        }

    }

}
