package com.yen.CA107G1;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.yen.CA107G1.HotelRoom.Activity_HotelRroomType_Detail;
import com.yen.CA107G1.Server.CommonTask;
import com.yen.CA107G1.Server.ServerURL;
import com.yen.CA107G1.Util.Util;
import com.yen.CA107G1.VO.HomePageVO;
import com.yen.CA107G1.VO.HotelroomtypemsgVO;
import com.yen.CA107G1.VO.NewsVO;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class Fragment_HomePage extends Fragment {
    private Banner banner;
    private ImageView homePageImg;
    private PagerSnapHelper snapHelper;
    private RecyclerView recyclerView, newsRc;
    private  CommonTask getNewsTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hompage, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        newsRc = view.findViewById(R.id.newsRcView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));

        newsRc.setHasFixedSize(true);
newsRc.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

        List<HomePageVO> hpList = new ArrayList<>();
        hpList.add(new HomePageVO(R.drawable.icon_member, "會員資料"));
        hpList.add(new HomePageVO(R.drawable.icon_likes, "我的收藏"));
        hpList.add(new HomePageVO(R.drawable.icon_pet, "寵物資料"));
        hpList.add(new HomePageVO(R.drawable.icon_order, "我的訂單"));



        snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(new HomePageAdapter(hpList));


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        newsRc.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.networkConnected(getActivity())) {

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAll");
            String jsonOut = jsonObject.toString();
            updateUI(jsonOut);
            Log.e("我是ROOMTYPE的onStart", "我在這");
        } else {
            Toast.makeText(getActivity(), "no network connection avaliable", Toast.LENGTH_SHORT);
        }
    }

    public void updateUI(String jsonOut) {
        getNewsTask = new CommonTask(ServerURL.News_URL, jsonOut);
        List<NewsVO> newsList = null;

        try {
            String jsonIn = getNewsTask.execute().get();
            Type listType = new TypeToken<List<NewsVO>>() {}.getType();
            newsList = new Gson().fromJson(jsonIn, listType);

        } catch (Exception e) {
            Log.e("我是RoomtypeDetail的", e.toString());
        }
        if (newsList == null || newsList.isEmpty()) {
            Toast.makeText(getActivity(), "NewsList not found", Toast.LENGTH_SHORT);
            Log.e("我是NewsAdapter", "Adapter沒有被填充");

        } else {
            newsRc.setAdapter(new Fragment_HomePage.NewsAdapter(getActivity(), newsList));
            Log.e("ROOMTYPEDETAIL的updateUI", "我是ROOMTYPE的MSG");
        }

    }

    private class HomePageAdapter extends RecyclerView.Adapter<HomePageAdapter.ViewHolder> {
        private List<HomePageVO> hpList;
        LayoutInflater inflater;

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

    private class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
        private List<NewsVO> newsList;
        private LayoutInflater inflater;

        private NewsAdapter(Context context, List<NewsVO> newsList) {
            this.newsList = newsList;
            inflater = LayoutInflater.from(context);
        }


        class ViewHolder extends RecyclerView.ViewHolder {
            TextView newsText;

            private ViewHolder(@NonNull View itemView) {
                super(itemView);
                newsText = itemView.findViewById(R.id.newsText);
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.card_news, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final NewsVO newsVO = newsList.get(position);
            holder.newsText.setText("\uD83D\uDE38"+newsVO.getNews_text());
        }

        @Override
        public int getItemCount() {
            return newsList.size();
        }


    }

}
