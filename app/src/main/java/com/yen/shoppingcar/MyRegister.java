package com.yen.shoppingcar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yen.shoppingcar.myServer.CommonTask;
import com.yen.shoppingcar.myServer.ServerURL;

import java.util.concurrent.ExecutionException;

public class MyRegister extends AppCompatActivity {
    private EditText registUser, registPassword, checkPasswrod,
            registName, registEmail, registPostnum, registAddress, registPhone;
    private TextView registTexView;
    private RadioButton radioMale, radioFemale;
    private RadioGroup radioGender;
    private CommonTask isUserIdExistTask, memberRegisterTask;
    private boolean memberExist = false;
    private Button btnSubmit, btnCancel;
    private Integer gender;
    private Integer mem_status = 1, mem_points = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_register);
        findViews();

    }

    void findViews() {

        registUser = findViewById(R.id.registUser);
        registPassword = findViewById(R.id.registPassword);
        checkPasswrod = findViewById(R.id.checkPassword);
        registName = findViewById(R.id.registName);
        registEmail = findViewById(R.id.registEmail);
        registPostnum = findViewById(R.id.registPostnum);
        registAddress = findViewById(R.id.registAddress);
        registPhone = findViewById(R.id.registPhone);
        radioGender = findViewById(R.id.radioGender);
        radioMale = findViewById(R.id.radioMale);
        radioFemale = findViewById(R.id.radioFemale);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnCancel = findViewById(R.id.btnCancel);
        registTexView = findViewById(R.id.registTexView);
        radioMale.setText("男");
        radioFemale.setText("女");


        registUser.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (Util.networkConnected(MyRegister.this)) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("action", "isUserIdExist");
                        jsonObject.addProperty("userId", registUser.getText().toString().trim());
                        String jsonOut = jsonObject.toString();
                        isUserIdExistTask = new CommonTask(ServerURL.Member_URL, jsonOut);
                        try {
                            String result = isUserIdExistTask.execute().get();
                            memberExist = Boolean.valueOf(result);
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (memberExist) {
                            registUser.setError("用戶已存在");
                            registUser.setText(null);
                        }
                    }
                }

            }
        });

        radioGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioMale:
                        gender = 1;
                    case R.id.radioFemale:
                        gender = 0;
                }
            }
        });


    }


    public void onSubmitClick(View view) {
        String mem_account = registUser.getText().toString().trim();
        String mem_password = registPassword.getText().toString().trim();
        String checkPassword = checkPasswrod.getText().toString().trim();
        String mem_name = registName.getText().toString().trim();
        String mem_email = registEmail.getText().toString().trim();
        String mem_phone_no = registPhone.getText().toString().trim();
        String mem_postnum = registPostnum.getText().toString().trim();
        String mem_address = registAddress.getText().toString().trim();
        Integer mem_gender = gender;

        String message = "";
        boolean isInputValid = true;
        if (memberExist) {
            registUser.setError("用戶已存在");
            message += "用戶已存在\n";
            isInputValid = false;
        }
        if (mem_account.isEmpty()) {
            registUser.setError("請輸入帳號");
            message += "請輸入帳號\n";
            isInputValid = false;
        }
        if (mem_password.isEmpty()) {
            registPassword.setError("請輸入密碼");
            message += "請輸入密碼\n";
            isInputValid = false;
        }
        if (!checkPassword.equals(mem_password)) {
            checkPasswrod.setError("請再次輸入密碼");
            message += "請再次輸入密碼\n";
            isInputValid = false;
        }

        if (mem_phone_no.isEmpty()) {
            Toast.makeText(this, "請輸入電話", Toast.LENGTH_SHORT).show();
            message += "請輸入電話\n";
            isInputValid = false;
        }
        if (mem_email.isEmpty()) {
            Toast.makeText(this, "請輸入電子信箱", Toast.LENGTH_SHORT).show();
            message += "請輸入電子信箱\n";
            isInputValid = false;
        }
        if (mem_postnum.isEmpty()) {
            Toast.makeText(this, "請輸入郵遞區號", Toast.LENGTH_SHORT).show();
            message += "請輸入郵遞區號\n";
            isInputValid = false;
        }
        if (mem_address.isEmpty()) {
            Toast.makeText(this, "請輸入地址", Toast.LENGTH_SHORT).show();
            message += "請輸入地址";
            isInputValid = false;
        }
        registTexView.setText(message);
//        MemberVO memberVO = new MemberVO(mem_status, mem_points, mem_account, mem_password, mem_name, mem_email, mem_phone_no,  mem_postnum, mem_address,mem_gender );
//        if (isInputValid) {
//            if (Util.networkConnected(this)) {
//                JsonObject jsonObject = new JsonObject();
//                jsonObject.addProperty("action", "insert");
//                jsonObject.addProperty("memberVO", new Gson().toJson(memberVO));
//                String jsonOut = jsonObject.toString();
//                memberRegisterTask = new CommonTask(ServerURL.Member_URL, jsonOut);
//                boolean isSuccess = false;
//                try {
//                    String result = memberRegisterTask.execute().get();
//                    isSuccess = Boolean.valueOf(result);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Log.e("連線有問題吧！？", e.toString());
//                }
//                if (isSuccess) {
//                    SharedPreferences sharedPreferences = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
//                    sharedPreferences.edit().putBoolean("login", true)
//                            .putString("userId", mem_account)
//                            .putString("password", mem_password)
//                            .apply();
//                    Toast.makeText(this,"用戶創建成功", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(MyRegister.this, MyMainActivity.class);
//                    startActivity(intent);
//                } else {
//                    Toast.makeText(this, "用戶創建失敗", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isUserIdExistTask != null) {
            isUserIdExistTask.cancel(true);
        }
        if (memberRegisterTask != null) {
            memberRegisterTask.cancel(true);
        }
    }
}
