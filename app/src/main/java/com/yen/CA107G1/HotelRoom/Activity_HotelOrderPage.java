package com.yen.CA107G1.HotelRoom;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.yen.CA107G1.R;
import com.yen.CA107G1.Util.Util;
import com.yen.CA107G1.VO.HotelOrderVO;
import com.yen.CA107G1.VO.HotelRoomTypeVO;
import com.yen.CA107G1.VO.MemberVO;
import com.yen.CA107G1.VO.PetNameVO;
import com.yen.CA107G1.Server.CommonTask;
import com.yen.CA107G1.Server.ServerURL;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Activity_HotelOrderPage extends AppCompatActivity {
    private ImageView selectedRoomType;
    private static TextView showCheckin, showCheckout;
    private TextView orderPrice;
    private Button checkinPicker, checkoutPicker, insertRoomOrder;
    private Spinner petSpinner;
    private CommonTask petNameTask;
    private static Integer checkInyear, checkInmonth, checkInday;
    private static Integer checkOutyear, checkOutmonth, checkOutday;
    private SharedPreferences sharedPreferences;
    private MemberVO memberVO;
    private static Calendar checkInCalendar, checkOutCalendar;
    private HotelRoomTypeVO hotelRoomTypeVO;
    private HotelOrderVO hotelOrderVO;
    private MemberVO member;
    private byte bitmap[];
    private Bitmap bmp;
    private EditText sendAddress;
    private List<PetNameVO> petNameList = null;
    private long totalDay;
    private Integer totalPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_order_page);
        findViews();
        showRightNow();
        hotelRoomTypeVO = (HotelRoomTypeVO) this.getIntent().getSerializableExtra("roomTypeVO");
        //取得前intet過來的圖片
        byte bitmap[] = (byte[]) getIntent().getSerializableExtra("roomTypeBitmap");
        bmp = BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length);
        selectedRoomType.setImageBitmap(bmp);

        sharedPreferences = getSharedPreferences(
                Util.PREF_FILE, MODE_PRIVATE);
        //取回並還原MemberVO
        String memberVO = sharedPreferences.getString("member", "");
        Toast.makeText(this, memberVO, Toast.LENGTH_SHORT).show();
        Gson gson = new Gson();
        member = gson.fromJson(memberVO, MemberVO.class);
    }

    public void findViews() {
        selectedRoomType = findViewById(R.id.selectedRoomType);
        showCheckin = findViewById(R.id.showCheckin);
        showCheckout = findViewById(R.id.showCheckout);
        checkinPicker = findViewById(R.id.checkinPicker);
        sendAddress = findViewById(R.id.sendAddress);
        checkoutPicker = findViewById(R.id.checkoutPicker);
        insertRoomOrder = findViewById(R.id.insertRoomOrder);
//        orderPrice = findViewById(R.id.orderPrice);
        petSpinner = findViewById(R.id.petSpinner);
    }

    private static void showRightNow() {
//        Calendar c = Calendar.getInstance();
        checkInCalendar = Calendar.getInstance();
        checkInyear = checkInCalendar.get(Calendar.YEAR);
        checkInmonth = checkInCalendar.get(Calendar.MONTH);
        checkInday = checkInCalendar.get(Calendar.DAY_OF_MONTH);

        checkOutCalendar = Calendar.getInstance();
        checkOutyear = checkOutCalendar.get(Calendar.YEAR);
        checkOutmonth = checkOutCalendar.get(Calendar.MONTH);
        checkOutday = checkOutCalendar.get(Calendar.DAY_OF_MONTH);

    }

    private static void ShowCheckIndateInfo() {
        showCheckin.setText(new StringBuilder().append(checkInyear)
                .append("-").append(parseNum(checkInmonth + 1))
                .append("-").append(parseNum(checkInday)));

    }

    private static void ShowCheckOutdateInfo() {
        showCheckout.setText(new StringBuilder().append(checkOutyear)
                .append("-").append(parseNum(checkOutmonth + 1))
                .append("-").append(parseNum(checkOutday)));
    }

    private static String parseNum(int day) {
        if (day >= 10)
            return String.valueOf(day);
        else
            return "0" + String.valueOf(day);
    }


    public void chooseCheckinDay(View view) {
        CheckInDatePickerFragment datePickerFragment = new CheckInDatePickerFragment();
        FragmentManager checkinFM = getSupportFragmentManager();

        datePickerFragment.show(checkinFM, "checkInPicker");
    }

    public void chooseCheckoutDay(View view) {
        CheckOutDatePickerFragment datePickerFragment = new CheckOutDatePickerFragment();
        FragmentManager checkoutFM = getSupportFragmentManager();
        datePickerFragment.show(checkoutFM, "checkOutPicker");
    }


    public static class CheckInDatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        // 改寫此方法以提供Dialog內容
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // 建立DatePickerDialog物件
            // this為OnDateSetListener物件
            // year、month、day會成為日期挑選器預選的年月日
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getActivity(), this, checkInyear, checkInmonth, checkInday);
            DatePicker inPicker = datePickerDialog.getDatePicker();
            inPicker.setMinDate(System.currentTimeMillis() - 1000);
            return datePickerDialog;
        }


        @Override
        // 日期挑選完成會呼叫此方法，並傳入選取的年月日
        public void onDateSet(DatePicker datePicker, int y, int m, int d) {
            checkInyear = y;
            checkInmonth = m;
            checkInday = d;
            ShowCheckIndateInfo();
        }
    }

    public static class CheckOutDatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        // 改寫此方法以提供Dialog內容
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // 建立DatePickerDialog物件
            // this為OnDateSetListener物件
            // year、month、day會成為日期挑選器預選的年月日
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getActivity(), this, checkOutyear, checkOutmonth, checkOutday);
            DatePicker outPicker = datePickerDialog.getDatePicker();
            outPicker.setMinDate(System.currentTimeMillis() - 1000);
            return datePickerDialog;
        }

        @Override
        public void onDateSet(DatePicker view, int y, int m, int d) {
            checkOutyear = y;
            checkOutmonth = m;
            checkOutday = d;
            ShowCheckOutdateInfo();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (Util.networkConnected(this)) {
            try {
                Gson gson = new Gson();
                sharedPreferences = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
                String result = sharedPreferences.getString("member", null);
                memberVO = gson.fromJson(result, MemberVO.class);
                Log.e("我下面有過去嗎？111", memberVO.toString());
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "getPetName");
                jsonObject.addProperty("mem_no", memberVO.getMem_no());
 Log.e("我下面有過去嗎？", "1314520: "+jsonObject.toString());
 String jsonOut = gson.toJson(jsonObject);
                String jsonIn = new CommonTask(ServerURL.Pet_URL, gson.toJson(jsonObject)).execute().get();
Log.e("gson.toJson jsonObject？", gson.toJson(jsonObject));
Log.e("----------------？", "--12-4-2-45-45-7");
Log.e("我是jsonInjsonIn21212125？", jsonIn);

                Log.e("I am jsonIn", jsonIn);
//                Log.e("jsonIn545454", jsonIn.substring(1, jsonIn.length() - 1));
                Type listType = new TypeToken<List<PetNameVO>>() {
                }.getType();


                petNameList = new Gson().fromJson(jsonIn, listType);
Log.e("我下面有過去嗎？3345678", petNameList.toString());
                Log.e("i am peNameList", petNameList.toString());
                List<String> petName = new ArrayList<>();
                Log.e("我是鶴麟", String.valueOf(petNameList.size()));
                for (int i = 0; i < petNameList.size(); i++) {
                    petName.add(petNameList.get(i).getPet_name());
                    Log.e("我是鶴麟2號", petName.get(i).toString());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, petName);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                petSpinner.setAdapter(adapter);
            } catch (Exception e) {
                Log.e("我是 PetName Spinner 例外", e.toString());
            }

        }
    }



    public void insertOrder(View view) {

        try {
//        checkInyear, checkInmonth, checkInday
//        checkOutyear, checkOutmonth, checkOutday
//            Log.e("checkInyear", checkInyear.toString());
//            Log.e("checkInyear", checkInmonth.toString());
//            Log.e("checkInyear", checkInday.toString());

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String inDay = new StringBuilder().append(checkInyear).append("-").append((checkInmonth + 1)).append("-").append(checkInday).toString();
            String outDay = new StringBuilder().append(checkOutyear).append("-").append((checkOutmonth + 1)).append("-").append(checkOutday).toString();
            Date inDate = null;
            Date outDate = null;
            try {
                inDate = simpleDateFormat.parse(inDay);
                outDate = simpleDateFormat.parse(outDay);
            } catch (ParseException e) {
                e.printStackTrace();
                Log.e("inDay", inDay);
            }


            totalDay = (outDate.getTime() / (1000 * 24 * 60 * 60)) - (inDate.getTime() / (1000 * 24 * 60 * 60));
            int orderprice = hotelRoomTypeVO.getH_roomtype_price();
            totalPrice = orderprice * (int)totalDay;
//        orderPrice.setText(totalPrice + "$");


            SimpleDateFormat toOrderDate = new SimpleDateFormat("yyyy-MM-dd");


            java.util.Date startDay = null;
            java.util.Date endDay = null;

            try {

                startDay = toOrderDate.parse(showCheckin.getText().toString());
                endDay = toOrderDate.parse(showCheckout.getText().toString());

            } catch (ParseException e) {
                e.printStackTrace();
            }

            long currentTime = System.currentTimeMillis();
            java.sql.Timestamp toDay = new java.sql.Timestamp(currentTime);

            java.sql.Date ord_date_start = new java.sql.Date(startDay.getTime());
            java.sql.Date ord_date_end = new java.sql.Date(endDay.getTime());

            Integer p = petSpinner.getSelectedItemPosition();
            Log.e("我是負的嗎", p.toString());
            PetNameVO petNameVO = petNameList.get(p);
            String pet_no = petNameVO.getPet_no();


            hotelOrderVO = new HotelOrderVO();
//        hotelOrderVO.setH_ord_no(hotelRoomTypeVO.getH_roomtype_no());
            hotelOrderVO.setH_ord_status_no(0);
            hotelOrderVO.setH_room_no("HROOM20170404001");
            hotelOrderVO.setMem_no(member.getMem_no());
            hotelOrderVO.setH_ord_date_start(ord_date_start);
            hotelOrderVO.setH_ord_date_end(ord_date_end);
            hotelOrderVO.setH_ord_time(toDay);
            hotelOrderVO.setH_ord_pickup(1);
            hotelOrderVO.setH_ord_has_beauty(0);
            hotelOrderVO.setPet_no(pet_no);
            hotelOrderVO.setH_ord_address(sendAddress.getText().toString());
            hotelOrderVO.setH_ord_mem_points(0);
            hotelOrderVO.setH_ord_total(totalPrice);

            Gson gson = new Gson();
            String hoVO = gson.toJson(hotelOrderVO);
            sharedPreferences.edit().putString("hoVO", hoVO).apply();

            //產生AlertDialog
            new AlertDialog.Builder(Activity_HotelOrderPage.this).setTitle("確認要下訂嗎?")

                    .setMessage("總入住天數為" + totalDay + "天" + "\n" + "總共金額為: " + totalPrice + "元")
                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent = new Intent(Activity_HotelOrderPage.this, Activity_CredicardPay.class);
                            startActivity(intent);

                        }
                    })
                    .setNegativeButton("no", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).show();
        }catch (NullPointerException e){
            Toast.makeText(this, "請選擇日期", Toast.LENGTH_SHORT);
        }

    }


}
