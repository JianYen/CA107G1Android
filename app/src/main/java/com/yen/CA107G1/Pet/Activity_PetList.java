package com.yen.CA107G1.Pet;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.yen.CA107G1.Member.Activity_MemberLogin;
import com.yen.CA107G1.R;
import com.yen.CA107G1.Util.Util;
import com.yen.CA107G1.VO.MemberVO;
import com.yen.CA107G1.VO.PetVO;
import com.yen.CA107G1.Server.CommonTask;
import com.yen.CA107G1.Server.PetLisImageTask;
import com.yen.CA107G1.Server.ServerURL;

import java.lang.reflect.Type;
import java.util.List;

public class Activity_PetList extends AppCompatActivity {
    private CommonTask petListTask;
    private PetLisImageTask petImgTask;
    private RecyclerView petRecyclerview;
    private SharedPreferences loginSPF;
    private MemberVO member;
    AlertDialog alertDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypetlist);

        petRecyclerview = findViewById(R.id.petRecyclerview);
        petRecyclerview.setHasFixedSize(true);
        petRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        loginSPF = getSharedPreferences(Util.PREF_FILE, Context.MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        petRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.networkConnected(this)) {
            //從SharedPreference 取出MemberVO物件 再取出mem_no
            try {
                Gson gson = new Gson();
                String memberVO = loginSPF.getString("member", null);
                member = gson.fromJson(memberVO, MemberVO.class);
                String mem_no = member.getMem_no();

                Log.e("memNO", mem_no);

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "getAll");
                jsonObject.addProperty("mem_no", mem_no);
                String jsonOut = jsonObject.toString();
                updateUI(jsonOut);
                Log.e("我是PetListAdapter", "我在這");

            } catch (NullPointerException ne) {
                Toast.makeText(this, "目前還沒有新增寵物唷!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "no network connection avaliable", Toast.LENGTH_SHORT);
        }
    }

    public void updateUI(String jsonOut) {
        petListTask = new CommonTask(ServerURL.Pet_URL, jsonOut);
        List<PetVO> petVOList = null;
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

        try {
            String jsonIn = petListTask.execute().get();
            Log.e("我是PetVOList的JsonIn", jsonIn);
            Type listType = new TypeToken<List<PetVO>>() {
            }.getType();

//            Type listType = new TypeToken<List<PetVO>>() {}.getType();

            petVOList = gson.fromJson(jsonIn, listType);

            Log.e("我是PetVOList", petVOList.toString());


        } catch (Exception e) {
            Log.e("TAG", e.toString());
        }
        if (petVOList == null || petVOList.isEmpty()) {
            Toast.makeText(this, "petVOList not found", Toast.LENGTH_SHORT);
            Log.e("我是PetListAdapter2", "Adapter沒有被填充");

        } else {
            petRecyclerview.setAdapter(new Activity_PetList.PetListAdapter(this, petVOList));
            Log.e("i'aaaa", "iaaaaaaaaaaaaaa");
        }

    }


    private class PetListAdapter extends RecyclerView.Adapter<Activity_PetList.PetListAdapter.ViewHolder> {
        private LayoutInflater layoutInflater;
        private List<PetVO> petList;
        private int imageSize;
        private Context context;

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView petName, petBreed, petBirth;
            ImageView petImg;

            public ViewHolder(View itemView) {
                super(itemView);
                petName = itemView.findViewById(R.id.petName);
                petBreed = itemView.findViewById(R.id.petBreed);
                petBirth = itemView.findViewById(R.id.petBirth);
                petImg = itemView.findViewById(R.id.petImg);
            }
        }

        PetListAdapter(Context context, List<PetVO> petList) {
            this.context = context;
            this.petList = petList;
            layoutInflater = LayoutInflater.from(context);
            imageSize = getResources().getDisplayMetrics().widthPixels / 2;
        }


        @Override
        public Activity_PetList.PetListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.card_mypet, parent, false);
            return new Activity_PetList.PetListAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(Activity_PetList.PetListAdapter.ViewHolder holder, int position) {
            final PetVO petVOList = petList.get(position);
            String petNo = petVOList.getPet_no();
            try {
                petImgTask = new PetLisImageTask(ServerURL.Pet_URL, petNo, imageSize, holder.petImg);
                petImgTask.execute();
                holder.petName.setText(petVOList.getPet_name());
                holder.petBreed.setText(petVOList.getPet_breed());
                holder.petBirth.setText(petVOList.getPet_birth().toString());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Activity_PetList.this, Activity_PetDetail.class);
                        intent.putExtra("petVOList", petVOList);
                        startActivity(intent);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public int getItemCount() {
            return petList.size();
        }
    }

}
