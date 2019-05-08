package com.yen.CA107G1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yen.CA107G1.VO.MemberVO;
import com.yen.CA107G1.myServer.CallServlet;
import com.yen.CA107G1.myServer.CommonTask;
import com.yen.CA107G1.myServer.ServerURL;

import java.util.List;
import java.util.concurrent.ExecutionException;


public class MyLogin extends AppCompatActivity {
    private Button btnLogin, btnRegist;
    private EditText tvmemID, tvmemPass;
    private CommonTask isMemberTask;
    List<MemberVO> memberVO;
    JsonObject jsonObject;
    String user, password;
    TextInputLayout tilMemId, tilMemPsw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        btnRegist = findViewById(R.id.btnRegist);
        tvmemID = findViewById(R.id.tvmemID);
        tvmemPass = findViewById(R.id.tvmemPass);
        tilMemId = findViewById(R.id.tilMemId);
        tilMemPsw = findViewById(R.id.tilMemPsw);
        setResult(RESULT_CANCELED);

    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
        boolean login = sharedPreferences.getBoolean("login", false);
        if (login) {
            String userId = sharedPreferences.getString("userId", "");
            String password = sharedPreferences.getString("password", "");
            if (isMember(userId, password)) {
                setResult(RESULT_OK);
                finish();
            }
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if(requestCode )
//    }

    private boolean isMember(final String mem_account, final String mem_password) {
        boolean isMember = false;
        if (Util.networkConnected(this)) {
            String url = ServerURL.Member_URL;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "isMember");
            jsonObject.addProperty("mem_account", mem_account);
            jsonObject.addProperty("mem_password", mem_password);
            String jsonOut = jsonObject.toString();
            isMemberTask = new CommonTask(url, jsonOut);
            try {
                String result = isMemberTask.execute().get();

                isMember = Boolean.valueOf(result);
            } catch (Exception e) {
                Log.e("ERRRRRRRRRROR", e.toString());
                isMember = false;
            }
        } else {
            Toast.makeText(this, "no connection", Toast.LENGTH_SHORT).show();
        }
        return isMember;
    }

    public void onLoginClick(View view) {
        String user = tvmemID.getText().toString().trim();
        String password = tvmemPass.getText().toString().trim();
        if (user.length() <= 0 || password.length() <= 0) {
            tilMemId.setError("請輸入帳號");
            tilMemPsw.setError("請輸入密碼");
            Toast.makeText(this, "帳號或密碼錯誤", Toast.LENGTH_SHORT).show();
            return;
        }//登入成功
        if (isMember(user, password)) {
            String memPic64 = null;
            try {
                memPic64 = new CallServlet().execute(ServerURL.MemberPic_URL, "action=memberPic&memAccount="+user+"&imageSize=300").get();
//                memPic64 = new CallServlet().execute("http://10.0.2.2:8081/CA107G1/member/ImageLoad","action=memberPic&memAccount=abc123&imageSize=300").get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            SharedPreferences sharedPreferences = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
            sharedPreferences.edit().putBoolean("login", true)
                    .putString("userId", user)
                    .putString("password", password)
                    .putString("memPic", memPic64 )
                    .apply();
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("userId", user);
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            finish();
        } else {//登入失敗
            Toast.makeText(this, "帳號或密碼錯誤", Toast.LENGTH_SHORT).show();
        }
    }

    public void btnRegist(View view) {
        Intent intent = new Intent(MyLogin.this, MyRegister.class);
        startActivity(intent);
    }


}
