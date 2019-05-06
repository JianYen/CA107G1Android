package com.yen.shoppingcar;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import com.yen.shoppingcar.VO.MemberVO;
import com.yen.shoppingcar.VO.PetVO;
import com.yen.shoppingcar.myServer.CommonTask;
import com.yen.shoppingcar.myServer.PetLisImageTask;
import com.yen.shoppingcar.myServer.ServerURL;

import java.lang.reflect.Type;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_MyPetList extends Fragment {
    private CommonTask petListTask;
    private PetLisImageTask petImgTask;
    private RecyclerView petRecyclerview;
    private SharedPreferences loginSPF;
    private MemberVO member;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mypetlist, container, false);

        petRecyclerview = view.findViewById(R.id.petRecyclerview);
        petRecyclerview.setHasFixedSize(true);
        petRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        loginSPF = getActivity().getSharedPreferences(Util.PREF_FILE, Context.MODE_PRIVATE);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        petRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.networkConnected(getActivity())) {
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

            } catch (NullPointerException ne){
               new AlertDialog.Builder(getActivity()).setTitle("貼心提醒!!")

                        .setMessage("登入才能查看更多資訊哦^_^")
                        .setPositiveButton("登入去", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(getActivity(), MyLogin.class);
                                startActivity(intent);
                                dialog.cancel();

                            }
                        })
                        .setNegativeButton("等會再去", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show();
            }
        } else {
            Toast.makeText(getActivity(), "no network connection avaliable", Toast.LENGTH_SHORT);
        }
    }

    public void updateUI(String jsonOut) {
        petListTask = new CommonTask(ServerURL.Pet_URL, jsonOut);
        List<PetVO> petVOList = null;
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

        try {
            String jsonIn = petListTask.execute().get();
            Log.e("我是PetVOList的JsonIn", jsonIn);
            Type listType = new TypeToken<List<PetVO>>() {}.getType();

//            Type listType = new TypeToken<List<PetVO>>() {}.getType();

            petVOList = gson.fromJson(jsonIn, listType);

            Log.e("我是PetVOList", petVOList.toString());


        } catch (Exception e) {
            Log.e("TAG", e.toString());
        }
        if (petVOList == null || petVOList.isEmpty()) {
            Toast.makeText(getActivity(), "petVOList not found", Toast.LENGTH_SHORT);
            Log.e("我是PetListAdapter2", "Adapter沒有被填充");

        } else {
            petRecyclerview.setAdapter(new PetListAdapter(getActivity(), petVOList));
            Log.e("i'aaaa", "iaaaaaaaaaaaaaa");
        }

    }


    private class PetListAdapter extends RecyclerView.Adapter<PetListAdapter.ViewHolder> {
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
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.card_mypet, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final PetVO petVOList = petList.get(position);
            String petNo = petVOList.getPet_no();
            petImgTask = new PetLisImageTask(ServerURL.Pet_URL, petNo, imageSize, holder.petImg);
            petImgTask.execute();

            holder.petName.setText(petVOList.getPet_name());
            holder.petBreed.setText(petVOList.getPet_breed());
            holder.petBirth.setText(petVOList.getPet_birth().toString());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), PetDetailActivity.class);
                    intent.putExtra("petVOList", petVOList);
                    startActivity(intent);
                }
            });


        }

        @Override
        public int getItemCount() {
            return petList.size();
        }
    }


}
