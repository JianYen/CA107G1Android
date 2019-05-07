package com.yen.shoppingcar.HotelRoom;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
import com.yen.shoppingcar.MyLogin;
import com.yen.shoppingcar.R;
import com.yen.shoppingcar.Util;
import com.yen.shoppingcar.VO.HotelRoomTypeVO;
import com.yen.shoppingcar.myServer.CommonTask;
import com.yen.shoppingcar.myServer.RoomTypeImageTask;
import com.yen.shoppingcar.myServer.ServerURL;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class Fragment_HotelPage extends Fragment {
    private static final String TAG = "HotelRoomType";
    private RecyclerView HotelRoomTypeRecyclerviewFG;
    private CommonTask getRoomTypeTask;
    private RoomTypeImageTask roomTypeImageTask;
    private SharedPreferences loginSPF;
    private Banner hotelBanner;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hotel_home_page, container, false);

        HotelRoomTypeRecyclerviewFG = view.findViewById(R.id.HotelRoomTypeRecyclerviewFG);
        HotelRoomTypeRecyclerviewFG.setHasFixedSize(true);
        HotelRoomTypeRecyclerviewFG.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

        loginSPF = getActivity().getSharedPreferences(Util.PREF_FILE, Context.MODE_PRIVATE);


        hotelBanner= view.findViewById(R.id.hotelBanner);
        hotelBanner.setImageLoader(new GlideImageLoader());
        List<Integer> list = new ArrayList<>();

        list.add(R.drawable.ad);
        list.add(R.drawable.ad2);
        list.add(R.drawable.ad3);
        hotelBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR).setImages(list).isAutoPlay(true)
        .setDelayTime(2000).start();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        HotelRoomTypeRecyclerviewFG.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

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
        getRoomTypeTask = new CommonTask(ServerURL.RoomType_URL, jsonOut);
        List<HotelRoomTypeVO> roomList = null;

        try {
            String jsonIn = getRoomTypeTask.execute().get();
            Type listType = new TypeToken<List<HotelRoomTypeVO>>() {}.getType();
            roomList = new Gson().fromJson(jsonIn, listType);

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        if (roomList == null || roomList.isEmpty()) {
            Toast.makeText(getActivity(), "roomTypeList not found", Toast.LENGTH_SHORT);
            Log.e("i'aaaa", "Adapter沒有被填充");

        } else {
            HotelRoomTypeRecyclerviewFG.setAdapter(new HotelRoomTypeAdapterFG(getActivity(), roomList));
            Log.e("i'aaaa", "我是ROOMTYPE的");
        }

    }

    class HotelRoomTypeAdapterFG extends RecyclerView.Adapter<HotelRoomTypeAdapterFG.MyViewHolder> {
        private Context context;
        private LayoutInflater layoutInflater;
        private List<HotelRoomTypeVO> roomTypelist;
        private int imageSize;



        HotelRoomTypeAdapterFG(Context context, List<HotelRoomTypeVO> roomTypelist) {
            this.context = context;
            layoutInflater = LayoutInflater.from(context);
            this.roomTypelist = roomTypelist;
            imageSize = getResources().getDisplayMetrics().widthPixels / 4;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.card_hotelroomtype, parent, false);
            return new MyViewHolder(itemView);
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView roomTypeImageViewFG;
            TextView roomTypeTitleFG, roomTypePriceFG;


            MyViewHolder(View itemView) {
                super(itemView);
                roomTypeImageViewFG = itemView.findViewById(R.id.roomTypeImageView);
                roomTypeTitleFG = itemView.findViewById(R.id.roomTypeTitle);
                roomTypePriceFG = itemView.findViewById(R.id.roomTypePrice);
            }
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            final HotelRoomTypeVO roomTypeVO = roomTypelist.get(position);
            String url = ServerURL.RoomType_URL;
            String roomTypeNo = roomTypeVO.getH_roomtype_no();
            roomTypeImageTask = new RoomTypeImageTask(url, roomTypeNo, imageSize, holder.roomTypeImageViewFG);
            Log.e("我是RoomTypeImg的", holder.roomTypeImageViewFG.toString());

            roomTypeImageTask.execute();

            holder.roomTypeTitleFG.setText(roomTypeVO.getH_roomtype_text());
            holder.roomTypePriceFG.setText("$".concat(String.valueOf(roomTypeVO.getH_roomtype_price())));


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (loginSPF.getBoolean("login", false ) == true) {
                        Intent intent = new Intent(getActivity(), HotelRroomType_Detail.class);
                        intent.putExtra("roomTypeVO", roomTypeVO);
                        startActivity(intent);
                    }else {
                        new AlertDialog.Builder(getActivity()).setTitle("請先登入")
                                .setMessage("您現在尚未登入～")
                                .setPositiveButton("GO,現在登入去", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(getActivity(), MyLogin.class);
                                        startActivity(intent);
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
            });



        }


        @Override
        public int getItemCount() {
            return roomTypelist.size();
        }




    }
    public class GlideImageLoader extends ImageLoader {


        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load((Integer) path).into(imageView);

        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (getRoomTypeTask != null) {
            getRoomTypeTask.cancel(true);
        }
        if (roomTypeImageTask != null) {
            roomTypeImageTask.cancel(true);
        }
    }

}
