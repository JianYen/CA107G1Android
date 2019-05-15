package com.yen.CA107G1.Emp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.yen.CA107G1.R;
import com.yen.CA107G1.Util.Util;
import com.yen.CA107G1.VO.SendBackVO;
import com.yen.CA107G1.Server.CommonTask;
import com.yen.CA107G1.Server.PetLisImageTask;
import com.yen.CA107G1.Server.ServerURL;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

public class Activity_EmpToDayOrder extends AppCompatActivity {
    private RecyclerView empDayOrder;
    private Button showMap;
    private CommonTask getPickUpTask;
    private TextView sendBackTitile;
    List<SendBackVO> pickUpList = null;
    private SharedPreferences SPF;
    private PetLisImageTask petImgTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_to_day_order);

        findViews();
        SPF = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
        empDayOrder.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
    }


    public void findViews() {
        empDayOrder = findViewById(R.id.empDayOrderRecyclerView);
        sendBackTitile = findViewById(R.id.sendBackTitile);
        empDayOrder.setHasFixedSize(true);
        showMap = findViewById(R.id.showMap);

    }

    @Override
    protected void onResume() {
        super.onResume();
        empDayOrder.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
    }

    public void onStart() {
        super.onStart();

        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getSendBack");
        getPickUpTask = new CommonTask(ServerURL.HotelOrder_URL, jsonObject.toString());

        try {
            String result = getPickUpTask.execute().get();
            Type listType = new TypeToken<List<SendBackVO>>() {
            }.getType();
            pickUpList = gson.fromJson(result, listType);
            SPF.edit().putString("hotelOrdList", pickUpList.toString());
            empDayOrder.setAdapter(new DayOrderAdapter(this, pickUpList));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            Toast.makeText(this, "今天沒有要送回的訂單^_^", Toast.LENGTH_SHORT).show();
        }
        if (pickUpList.size() != 0) {
            sendBackTitile.setText("今天總共有" + pickUpList.size() + "筆要送回");
            Toast.makeText(this, "今天總共有" + pickUpList.size() + "筆要送回的訂單呦^_^", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "今天沒有要送回的訂單^_^", Toast.LENGTH_LONG).show();
            Log.e("PickupList是空集合", pickUpList.toString());
        }

    }

    public void goMap(View view) {

        try {
            Gson gson = new Gson();
            String hOrdList = gson.toJson(pickUpList);
            Intent intent = new Intent(Activity_EmpToDayOrder.this, Activity_Maps.class);
            Log.e("我是Today", hOrdList);
            intent.putExtra("hOrdList", hOrdList);
            startActivity(intent);
        } catch (NullPointerException e) {
            Toast.makeText(this, "今天沒有要送回的訂單^_^", Toast.LENGTH_SHORT).show();
        }
    }


    private class DayOrderAdapter extends RecyclerView.Adapter<DayOrderAdapter.ViewHolder> {
        private List<SendBackVO> list;
        private LayoutInflater layoutInflater;
        private int imageSize;
        private Context context;

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView orderMemName, orderPetName, orderAddress, orderNo;
            private CircleImageView orderPetImage;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                orderMemName = itemView.findViewById(R.id.orderMemName);
                orderPetName = itemView.findViewById(R.id.orderPetName);
                orderAddress = itemView.findViewById(R.id.orderAddress);
                orderNo = itemView.findViewById(R.id.orderNo);
                orderPetImage = itemView.findViewById(R.id.orderPetImage);
            }
        }

        DayOrderAdapter(Context context, List<SendBackVO> list) {
            this.list = list;
            this.context = context;
            layoutInflater = LayoutInflater.from(context);
            imageSize = getResources().getDisplayMetrics().widthPixels / 2;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = layoutInflater.from(parent.getContext()).inflate(R.layout.card_empdayorder, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final SendBackVO sendBackVO = list.get(position);
            holder.orderMemName.setText(sendBackVO.getMem_name());
            holder.orderPetName.setText(sendBackVO.getPet_name());
            holder.orderAddress.setText(sendBackVO.getH_ord_address());
            holder.orderNo.setText(sendBackVO.getH_ord_no());
            String petNo = sendBackVO.getPet_no();
            petImgTask = new PetLisImageTask(ServerURL.Pet_URL, petNo, imageSize, holder.orderPetImage);
            petImgTask.execute();
        }

        @Override
        public int getItemCount() {
            return list.size();
        }


    }
}
