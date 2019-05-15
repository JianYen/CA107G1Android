package com.yen.CA107G1.HotelRoom;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hannesdorfmann.swipeback.Position;
import com.hannesdorfmann.swipeback.SwipeBack;
import com.yen.CA107G1.R;
import com.yen.CA107G1.Server.CommonTask;
import com.yen.CA107G1.Util.Util;
import com.yen.CA107G1.VO.HomePageVO;
import com.yen.CA107G1.VO.HotelRoomTypeVO;
import com.yen.CA107G1.Server.RoomTypeImageTask;
import com.yen.CA107G1.Server.ServerURL;
import com.yen.CA107G1.VO.HotelroomtypemsgVO;
import com.yen.CA107G1.VO.PetVO;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Activity_HotelRroomType_Detail extends AppCompatActivity {
    private RoomTypeImageTask roomTypeImageTask;
    private HotelRoomTypeVO hotelRoomTypeVO;
    private Bitmap bitmap = null;
    private byte buff[] = null;
    private RecyclerView hotelMsgRcView;
    private HotelroomtypemsgVO hotelroomtypemsgVO;
    private String hotelMsgResult;
    private CommonTask getMsgTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotelroomtype_detail);
        hotelMsgRcView = findViewById(R.id.hotelMsgRcView);

        hotelRoomTypeVO = (HotelRoomTypeVO) getIntent().getSerializableExtra("roomTypeVO");
        if (hotelRoomTypeVO == null) {
            Toast.makeText(this, "房型詳情無法顯示", Toast.LENGTH_SHORT);
        } else {
            showDetail(hotelRoomTypeVO);
        }

        SwipeBack.attach(this, Position.LEFT)
//                .setContentView(R.layout.activity_hotelroomtype_detail)
                .setSwipeBackView(R.layout.swipeback_default);

    }
    @Override
    public void onResume() {
        super.onResume();
        hotelMsgRcView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.networkConnected(this)) {

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getMsg");
            jsonObject.addProperty("roomTypeNo",hotelRoomTypeVO.getH_roomtype_no());
            String jsonOut = jsonObject.toString();
            Log.e("我是ROOMTYPE的onStart", jsonOut);
            updateUI(jsonOut);
            Log.e("我是ROOMTYPE的onStart", "我在這");
            Log.e("我是ROOMTYPE的onStart", hotelRoomTypeVO.getH_roomtype_no());
        } else {
            Toast.makeText(this, "no network connection avaliable", Toast.LENGTH_SHORT);
        }
    }

    public void updateUI(String jsonOut) {
        getMsgTask = new CommonTask(ServerURL.HotelRoomTypeMsg_URL, jsonOut);
        List<HotelroomtypemsgVO> msgList = null;

        try {
            String jsonIn = getMsgTask.execute().get();
            Type listType = new TypeToken<List<HotelroomtypemsgVO>>() {}.getType();
            msgList = new Gson().fromJson(jsonIn, listType);

        } catch (Exception e) {
            Log.e("我是RoomtypeDetail的", e.toString());
        }
        if (msgList == null || msgList.isEmpty()) {
            Toast.makeText(this, "roomTypeList not found", Toast.LENGTH_SHORT);
            Log.e("RoomDetail的UpDATEUI", "Adapter沒有被填充");

        } else {
            hotelMsgRcView.setAdapter(new Activity_HotelRroomType_Detail.HotelMsgAdapter(this, msgList));
            Log.e("ROOMTYPEDETAIL的updateUI", "我是ROOMTYPE的MSG");
        }

    }



    private byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    public void showDetail(final HotelRoomTypeVO hotelRoomTypeVO) {
        ImageView roomTypeDetailImage = findViewById(R.id.roomTypeDetailImage);
        String rtno = hotelRoomTypeVO.getH_roomtype_no();
        final int imageSize = getResources().getDisplayMetrics().widthPixels / 2;


        try {
            roomTypeImageTask = new RoomTypeImageTask(ServerURL.RoomType_URL, rtno, imageSize);
            bitmap = roomTypeImageTask.execute().get();

            //圖片包裝好 然後序列化帶到下個頁面
            buff = new byte[1024 * 1024];
            buff = Bitmap2Bytes(bitmap);

        } catch (Exception e) {
            Log.e("ShowDetail", e.toString());
        }

        if (bitmap != null) {

            roomTypeDetailImage.setImageBitmap(bitmap);

        } else {
            roomTypeDetailImage.setImageResource(R.drawable.beauty2);
        }

        TextView roomTypeDetailTitle = findViewById(R.id.roomTypeDetailTitle);
        TextView roomTypeDetailContent = findViewById(R.id.roomTypeDetailContent);
        TextView roomTypeDetailPrice = findViewById(R.id.roomTypeDetailPrice);
        roomTypeDetailTitle.setText(hotelRoomTypeVO.getH_roomtype_text());
        roomTypeDetailContent.setText(hotelRoomTypeVO.getH_roomtype_desc());
        roomTypeDetailPrice.setText("\uD83D\uDCB2" + hotelRoomTypeVO.getH_roomtype_price());

        Button orderBtn = findViewById(R.id.orderBtn);
        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_HotelRroomType_Detail.this, Activity_HotelOrderPage.class);
                intent.putExtra("roomTypeVO", hotelRoomTypeVO);
                intent.putExtra("roomTypeBitmap", buff);
                startActivity(intent);
            }
        });
    }

    private class HotelMsgAdapter extends RecyclerView.Adapter<Activity_HotelRroomType_Detail.HotelMsgAdapter.ViewHolder> {
        private LayoutInflater layoutInflater;
        private List<HotelroomtypemsgVO> msgList;
        private Context context;

        private HotelMsgAdapter(Context context, List<HotelroomtypemsgVO> msgList) {
            this.context = context;
            this.msgList = msgList;
            layoutInflater = LayoutInflater.from(context);


        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private RatingBar hotelRating;
            private TextView msgHotelMem_name, hotelMSG;

            public ViewHolder(View itemView) {
                super(itemView);
                hotelRating = itemView.findViewById(R.id.hotelRating);
                msgHotelMem_name = itemView.findViewById(R.id.msgHotelMem_name);
                hotelMSG = itemView.findViewById(R.id.hotelMSG);
            }
        }

        @Override
        public Activity_HotelRroomType_Detail.HotelMsgAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.card_roomtyperating, parent, false);
            return new Activity_HotelRroomType_Detail.HotelMsgAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(Activity_HotelRroomType_Detail.HotelMsgAdapter.ViewHolder holder, int position) {
            final HotelroomtypemsgVO msgVO = msgList.get(position);
            holder.hotelMSG.setText(msgVO.getH_msg_text());
            holder.msgHotelMem_name.setText(msgVO.getMem_name());
            holder.hotelRating.setRating(msgVO.getH_msg_score());
        }

        @Override
        public int getItemCount() {
            return msgList.size();
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.swipeback_stack_to_front, R.anim.swipeback_slide_right_out);
    }
}
