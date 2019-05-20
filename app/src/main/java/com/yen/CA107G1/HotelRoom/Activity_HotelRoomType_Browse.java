package com.yen.CA107G1.HotelRoom;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.yen.CA107G1.R;
import com.yen.CA107G1.Util.Util;
import com.yen.CA107G1.VO.HotelRoomTypeVO;
import com.yen.CA107G1.Server.CommonTask;
import com.yen.CA107G1.Server.RoomTypeImageTask;
import com.yen.CA107G1.Server.ServerURL;

import java.lang.reflect.Type;
import java.util.List;

public class Activity_HotelRoomType_Browse extends AppCompatActivity {
    private static final String TAG = "HotelRoomType";
    private RecyclerView hotelRoomTypeRcview;
    private CommonTask getRoomTypeTask;
    private RoomTypeImageTask roomTypeImageTask;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotelroomtype_browse);
        hotelRoomTypeRcview = findViewById(R.id.HotelRoomTypeRecyclerview);
        hotelRoomTypeRcview.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Util.networkConnected(this)) {

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAll");
            String jsonOut = jsonObject.toString();
            updateUI(jsonOut);
        } else {
            Toast.makeText(this, "no network connection avaliable", Toast.LENGTH_SHORT);
        }
    }

    private void updateUI(String jsonOut) {
        getRoomTypeTask = new CommonTask(ServerURL.RoomType_URL, jsonOut);
        List<HotelRoomTypeVO> roomList = null;

        try {
            String jsonIn = getRoomTypeTask.execute().get();
            Type listType = new TypeToken<List<HotelRoomTypeVO>>() {
            }.getType();
            roomList = new Gson().fromJson(jsonIn, listType);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        if (roomList == null || roomList.isEmpty()) {
            Toast.makeText(this, "roomTypeList not found", Toast.LENGTH_SHORT);

        } else {
            hotelRoomTypeRcview.setAdapter(new HotelRoomTypeAdapter(this, roomList));
        }

    }

    private class HotelRoomTypeAdapter extends RecyclerView.Adapter<HotelRoomTypeAdapter.MyViewHolder> {
        private Context context;
        private LayoutInflater layoutInflater;
        private List<HotelRoomTypeVO> roomTypelist;
        private int imageSize;

        HotelRoomTypeAdapter(Context context, List<HotelRoomTypeVO> roomTypelist) {
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

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final HotelRoomTypeVO roomTypeVO = roomTypelist.get(position);
            String url = ServerURL.RoomType_URL;
            String roomTypeNo = roomTypeVO.getH_roomtype_no();
            roomTypeImageTask = new RoomTypeImageTask(url, roomTypeNo, imageSize, holder.roomTypeImageView);
            roomTypeImageTask.execute();

            holder.roomTypeTitle.setText(roomTypeVO.getH_roomtype_text());
            holder.roomTypePrice.setText("$".concat(String.valueOf(roomTypeVO.getH_roomtype_price())));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Activity_HotelRoomType_Browse.this, Activity_HotelRroomType_Detail.class);
                    intent.putExtra("roomTypeVO", roomTypeVO);
                    startActivity(intent);
                }
            });

        }


        @Override
        public int getItemCount() {
            return roomTypelist.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView roomTypeImageView, roomTypeLike;
            TextView roomTypeTitle, roomTypePrice;

            MyViewHolder(View itemView) {
                super(itemView);
                roomTypeImageView = itemView.findViewById(R.id.roomTypeImageView);
                roomTypeTitle = itemView.findViewById(R.id.roomTypeTitle);
                roomTypePrice = itemView.findViewById(R.id.roomTypePrice);
            }
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        if (getRoomTypeTask != null) {
            getRoomTypeTask.cancel(true);
        }
        if (roomTypeImageTask != null) {
            roomTypeImageTask.cancel(true);
        }
    }
}
