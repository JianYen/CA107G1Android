package com.yen.CA107G1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yen.CA107G1.Emp.Activity_EmpLogin;
import com.yen.CA107G1.Emp.Activity_EmpToDayOrder;
import com.yen.CA107G1.Emp.Activity_Maps;
import com.yen.CA107G1.HotelRoom.Fragment_HotelPage;
import com.yen.CA107G1.HotelRoom.Activity_HotelRoomType_Browse;
import com.yen.CA107G1.Member.Activity_MemberLogin;
import com.yen.CA107G1.Member.Activity_Member_Order;
import com.yen.CA107G1.Pet.Activity_PetList;
import com.yen.CA107G1.Shop.Activity_Cart;
import com.yen.CA107G1.Shop.Activity_MyShopItem_Browse;
import com.yen.CA107G1.Shop.Activity_MyShopItem_Detail;
import com.yen.CA107G1.Shop.Fragment_ShopHomePage;
import com.yen.CA107G1.Util.Util;
import com.yen.CA107G1.VO.MemberVO;
import com.yen.CA107G1.Server.CommonTask;
import com.yen.CA107G1.Server.ServerURL;

import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

public class Activity_Main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {
    private BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;
    private CircleImageView circleImageView;
    private Button navLogin;
    private TextView navName;
    private CommonTask getMemberVOTask;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private SharedPreferences loginSharePreference;
    private MemberVO memberVO;
    private final int LOGIN_REQUEST = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.drawer_notlogin);
//        navigationView.inflateHeaderView(R.layout.activity_left_drawer_header);
        navigationView.setNavigationItemSelectedListener(this);

        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(this);

        bottomNavigationView = findViewById(R.id.bottom_bar);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

//        addNavHeader();
    }


    //點擊底部導航欄，切換fragment頁面
    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            viewPager.setCurrentItem(0);
                            return true;
                        case R.id.nav_hotel:
                            viewPager.setCurrentItem(1);
                            return true;
                        case R.id.nav_shop:
                            viewPager.setCurrentItem(2);
                            return true;
                    }
                    return true;
                }

            };


    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int position) {
        bottomNavigationView.getMenu().getItem(position).setChecked(true);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    public void onNavLoginClick(View view) {


        SharedPreferences loginSharePreference = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);

        String userId = loginSharePreference.getString("userId", null);

        switch (navLogin.getText().toString()) {
            case "登入":
                Intent intent = new Intent(Activity_Main.this, Activity_MemberLogin.class);
                startActivityForResult(intent, LOGIN_REQUEST);
                break;

            case "登出":
                loginSharePreference.edit().clear().apply();
                drawerLayout.closeDrawer(GravityCompat.START);
                addNavHeader();
                break;

        }
    }

    public void addNavHeader() {
        View view = navigationView.getHeaderView(0);
        circleImageView = view.findViewById(R.id.circleImage);
        navName = view.findViewById(R.id.navName);
        navLogin = view.findViewById(R.id.navLogin);
        loginSharePreference = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
        String userId = loginSharePreference.getString("userId", null);
        if (userId != null) {
            navName.setText(userId);
            String memPic = loginSharePreference.getString("memPic", null);
            try {
                byte[] memPic64 = Base64.decode(memPic, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(memPic64, 0, memPic64.length);
                circleImageView.setImageBitmap(bitmap);
            } catch (NullPointerException e) {
                Log.e("我有東西嗎", e.toString());
                return;
            }
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.drawer_login);
            navLogin.setText("登出");
            getMemberVO();

        } else {
            navName.setText("尚未登入");
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.drawer_notlogin);
            circleImageView.setImageResource(R.drawable.ic_member);
            navLogin.setText("登入");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode != LOGIN_REQUEST) {
            return;
        }

        switch (resultCode) {
            case RESULT_OK:
                Bundle bundle = data.getExtras();
                String userId = bundle.getString("userId");
                navName.setText(userId);
                navLogin.setText("登出");

            case RESULT_CANCELED:
                navName.setText("尚未登入");
                navLogin.setText("登入");
        }
    }

    private class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new Fragment_HomePage();

                case 1:
                    return new Fragment_HotelPage();

                case 2:
                    return new Fragment_ShopHomePage();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        Intent intent = new Intent();
        // Handle navigation view item clicks here.
        switch (menuItem.getItemId()) {




            case R.id.nav_myPet:
                intent.setClass(Activity_Main.this, Activity_PetList.class);
                break;


            case R.id.nav_order:
                intent.setClass(Activity_Main.this, Activity_Member_Order.class);
                break;

            case R.id.nav_hotel:
                intent.setClass(Activity_Main.this, Activity_HotelRoomType_Browse.class);
                break;


            case R.id.empLogin:
                intent.setClass(Activity_Main.this, Activity_EmpLogin.class);
                break;


            case R.id.nav_cart:
                intent.setClass(Activity_Main.this, Activity_Cart.class);
                break;


        }
        startActivity(intent);

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        addNavHeader();

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void getMemberVO() {
        Gson gson = new Gson();
        String userId = loginSharePreference.getString("userId", null);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getByName");
        jsonObject.addProperty("userId", userId);
        String jsonOut = gson.toJson(jsonObject);
//        getMemberVOTask = new CommonTask(ServerURL.Member_URL, jsonOut);

        try {
            String result = new CommonTask(ServerURL.Member_URL, jsonOut).execute().get();
//            memberVO = gson.fromJson(result, MemberVO.class);

//            String member = gson.toJson((memberVO));
            loginSharePreference.edit().putString("member", result).apply();
//            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
//            Type list = new TypeToken<List<MemberVO>>(){}.getType();
//            member = gson.fromJson(jsonIn, list);
//            Log.e("IMMMMMMMMMMMMMMMMMMMM", memberVO.getMem_no());
//            Log.e("IMMMMMMMMMMMMMMMMMMMM", memberVO.getMem_address());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
