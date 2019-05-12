package com.yen.CA107G1.Member;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.yen.CA107G1.R;
import com.yen.CA107G1.Util.Util;
import com.yen.CA107G1.VO.MemberPetOrderVO;
import com.yen.CA107G1.VO.MemberVO;
import com.yen.CA107G1.Server.CommonTask;
import com.yen.CA107G1.Server.PetLisImageTask;
import com.yen.CA107G1.Server.ServerURL;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

public class Activity_Member_Order extends AppCompatActivity {
    private RecyclerView memberOrder;
    private Button showMap;
    private CommonTask getMemberPetOrderTask;
    List<MemberPetOrderVO> memberPetOrderVOList = null;
    private SharedPreferences SPF;
    private PetLisImageTask petImgTask;
    private MemberVO memberVO;
    private String mem_no=null;
    PagerSnapHelper snapHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memberorder);

        findViews();
        SPF = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
        String member = SPF.getString("member", null);
        Gson gson = new Gson();
        memberVO=gson.fromJson(member, MemberVO.class);
        mem_no = memberVO.getMem_no();
        memberOrder.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(memberOrder);
//        Window win = getWindow();
//        WindowManager.LayoutParams lp = win.getAttributes();
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        lp.dimAmount = 0.5f;
//        win.setAttributes(lp);
    }


    public void findViews() {
        memberOrder = findViewById(R.id.memberOrder);
        memberOrder.setHasFixedSize(true);

    }

    public void onStart() {
        super.onStart();

        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getMemberOrder");
        jsonObject.addProperty("mem_no",mem_no);
        getMemberPetOrderTask = new CommonTask(ServerURL.HotelOrder_URL, jsonObject.toString());

        try {
            String result = getMemberPetOrderTask.execute().get();
            Type listType = new TypeToken<List<MemberPetOrderVO>>() {}.getType();
            memberPetOrderVOList = gson.fromJson(result, listType);
            SPF.edit().putString("memberPetOrderVOList", memberPetOrderVOList.toString());
            memberOrder.setAdapter(new Activity_Member_Order.MemberOrderAdapter(this,memberPetOrderVOList));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }catch (NullPointerException e){
            Toast.makeText(this, "今天沒有要送回的訂單^_^", Toast.LENGTH_SHORT).show();
        }

        Log.e("I am pickUpList", memberPetOrderVOList.toString());
    }




    private class MemberOrderAdapter extends RecyclerView.Adapter<Activity_Member_Order.MemberOrderAdapter.ViewHolder> {
        private List<MemberPetOrderVO> list;
        private LayoutInflater layoutInflater;
        private int imageSize;
        private Context context;

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

        MemberOrderAdapter(Context context , List<MemberPetOrderVO> list) {
            this.list = list;
            this.context = context;
            layoutInflater = LayoutInflater.from(context);
            imageSize = getResources().getDisplayMetrics().widthPixels / 2;
        }

        @NonNull
        @Override
        public Activity_Member_Order.MemberOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = layoutInflater.from(parent.getContext()).inflate(R.layout.card_empdayorder, parent, false);
            return new Activity_Member_Order.MemberOrderAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Activity_Member_Order.MemberOrderAdapter.ViewHolder holder, int position) {
            final MemberPetOrderVO memberPetOrderVO = list.get(position);
            holder.orderMemName.setText(memberPetOrderVO.getMem_name());
            holder.orderPetName.setText(memberPetOrderVO.getPet_name());
            holder.orderAddress.setText(memberPetOrderVO.getH_ord_address());
            holder.orderNo.setText(memberPetOrderVO.getH_ord_no());
            String petNo = memberPetOrderVO.getPet_no();
            petImgTask = new PetLisImageTask(ServerURL.Pet_URL, petNo, imageSize, holder.orderPetImage);
            petImgTask.execute();
        }

        @Override
        public int getItemCount() {
            return list.size();
        }


    }
}
