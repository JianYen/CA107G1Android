package com.yen.CA107G1.HotelRoom;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yen.CA107G1.R;
import com.yen.CA107G1.VO.HotelRoomTypeVO;
import com.yen.CA107G1.Server.RoomTypeImageTask;
import com.yen.CA107G1.Server.ServerURL;

import java.io.ByteArrayOutputStream;

public class Activity_HotelRroomType_Detail extends AppCompatActivity {
    private RoomTypeImageTask roomTypeImageTask;
    private HotelRoomTypeVO hotelRoomTypeVO;
    private Bitmap bitmap = null;
    private byte buff[] = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotelroomtype_detail);

        hotelRoomTypeVO = (HotelRoomTypeVO)getIntent().getSerializableExtra("roomTypeVO");
        if (hotelRoomTypeVO == null) {
            Toast.makeText(this, "房型詳情無法顯示", Toast.LENGTH_SHORT);
        } else {
            showDetail(hotelRoomTypeVO);
        }
    }

    private byte[] Bitmap2Bytes(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    public void showDetail(final HotelRoomTypeVO hotelRoomTypeVO){
        ImageView roomTypeDetailImage = findViewById(R.id.roomTypeDetailImage);
        String rtno =hotelRoomTypeVO.getH_roomtype_no();
        final int imageSize = getResources().getDisplayMetrics().widthPixels / 2;


        try {
            roomTypeImageTask = new RoomTypeImageTask(ServerURL.RoomType_URL, rtno, imageSize);
            bitmap = roomTypeImageTask.execute().get();

            //圖片包裝好 然後序列化帶到下個頁面
            buff =new byte[1024*1024];
            buff = Bitmap2Bytes(bitmap);

        } catch (Exception e) {
            Log.e("ShowDetail", e.toString());
        }

        if (bitmap != null) {

            roomTypeDetailImage.setImageBitmap(bitmap);

        }else {
            roomTypeDetailImage.setImageResource(R.drawable.beauty2);
        }

        TextView roomTypeDetailTitle = findViewById(R.id.roomTypeDetailTitle);
        TextView roomTypeDetailContent = findViewById(R.id.roomTypeDetailContent);
        TextView roomTypeDetailPrice = findViewById(R.id.roomTypeDetailPrice);
        roomTypeDetailTitle.setText(hotelRoomTypeVO.getH_roomtype_text());
        roomTypeDetailContent.setText(hotelRoomTypeVO.getH_roomtype_desc());
        roomTypeDetailPrice.setText("$"+hotelRoomTypeVO.getH_roomtype_price());

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
}
