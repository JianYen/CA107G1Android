package com.yen.shoppingcar;

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

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.yen.shoppingcar.VO.SendBackVO;
import com.yen.shoppingcar.myServer.CommonTask;
import com.yen.shoppingcar.myServer.ServerURL;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

public class EmpToDayOrder extends AppCompatActivity {
    private RecyclerView empDayOrder;
    private Button showMap;
    private CommonTask getPickUpTask;
    List<SendBackVO> pickUpList = null;
    private SharedPreferences SPF;

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
        empDayOrder.setHasFixedSize(true);
        showMap = findViewById(R.id.showMap);

    }

    public void onStart() {
        super.onStart();

        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getSendBack");
        getPickUpTask = new CommonTask(ServerURL.HotelOrder_URL, jsonObject.toString());

        try {
            String result = getPickUpTask.execute().get();
            Type listType = new TypeToken<List<SendBackVO>>() {}.getType();
            pickUpList = gson.fromJson(result, listType);
            SPF.edit().putString("hotelOrdList", pickUpList.toString());
            empDayOrder.setAdapter(new DayOrderAdapter(pickUpList));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.e("I am pickUpList", pickUpList.toString());
    }

    public void goMap(View view) {
        showMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                String hOrdList = gson.toJson(pickUpList);
                Intent intent =new Intent(EmpToDayOrder.this, MapsActivity.class);
                intent.putExtra("hOrdList", hOrdList);
                startActivity(intent);
            }
        });
    }


    private class DayOrderAdapter extends RecyclerView.Adapter<DayOrderAdapter.ViewHolder> {
        private List<SendBackVO> list;
        private LayoutInflater layoutInflater;

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView orderMemName, orderPetName,orderAddress,orderNo;
            private CircleImageView orderPetImage;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                orderMemName=itemView.findViewById(R.id.orderMemName);
                orderPetName = itemView.findViewById(R.id.orderPetName);
                orderAddress = itemView.findViewById(R.id.orderAddress);
                orderNo=itemView.findViewById(R.id.orderNo);
                orderPetImage=itemView.findViewById(R.id.orderPetImage);
            }
        }

        DayOrderAdapter(List<SendBackVO> list) {
            this.list = list;
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
        }

        @Override
        public int getItemCount() {
            return list.size();
        }


    }
}
