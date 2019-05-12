package com.yen.CA107G1.Emp;

import android.app.AppComponentFactory;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yen.CA107G1.R;
import com.yen.CA107G1.Server.CallServlet;
import com.yen.CA107G1.Server.CommonTask;
import com.yen.CA107G1.Server.ServerURL;
import com.yen.CA107G1.Util.Util;

import java.util.concurrent.ExecutionException;

public class Activity_EmpLogin extends AppCompatActivity {
    private EditText tvEmpId, tvEmpPsd;
    private TextInputLayout tilEmpId, tilEmpPsw;
    private Button btnEmpLogin;
    private SharedPreferences sharedPreferences;
    private CommonTask isEmpTask;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
setContentView(R.layout.activity_emplogin);
        findViews();
    }

    public void findViews() {
        tvEmpId = findViewById(R.id.tvEmpId);
        tvEmpPsd = findViewById(R.id.tvEmpPsd);
        tilEmpId = findViewById(R.id.tilEmpId);
        tilEmpPsw = findViewById(R.id.tilEmpPsw);
        btnEmpLogin = findViewById(R.id.btnEmpLogin);
    }

    @Override
    protected void onStart() {
        super.onStart();
        sharedPreferences = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
        boolean empLogin = sharedPreferences.getBoolean("empLogin", false);
        if (empLogin) {
            String empId = sharedPreferences.getString("empId", "");
            String empPsd = sharedPreferences.getString("empPsd", "");
            if (isEmp(empId, empPsd)) {
                setResult(RESULT_OK);
                finish();
            }
        }

    }

    private boolean isEmp(final String empNo, final String empPsd) {
        boolean isEmp = false;
        if (Util.networkConnected(this)) {
            String url = ServerURL.Emp_URL;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "isEmp");
            jsonObject.addProperty("empNo", empNo);
            jsonObject.addProperty("empPsd", empPsd);
            String jsonOut = jsonObject.toString();
            isEmpTask = new CommonTask(url, jsonOut);
            try {
                String result = isEmpTask.execute().get();

                isEmp = Boolean.valueOf(result);
            } catch (Exception e) {
                Log.e("ERRRRRRRRRROR", e.toString());
                isEmp = false;
            }
        } else {
            Toast.makeText(this, "no connection", Toast.LENGTH_SHORT).show();
        }
        return isEmp;
    }

    public void onEmpLogin(View view) {

        String empNo = tvEmpId.getText().toString().trim();
        String empPsd = tvEmpPsd.getText().toString().trim();

        if (empNo.length() <= 0 || empPsd.length() <= 0) {
            tilEmpId.setError("請輸入帳號");
            tilEmpPsw.setError("請輸入密碼");
            Toast.makeText(this, "帳號或密碼錯誤", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isEmp(empNo, empPsd)) {

            sharedPreferences.edit().putBoolean("empLogin", true)
                    .putString("empNo", empNo)
                    .putString("empPsd", empPsd)
                    .apply();


            Intent intent = new Intent(Activity_EmpLogin.this, Activity_EmpHomePage.class);
            startActivity(intent);
        } else {//登入失敗
            Toast.makeText(this, "帳號或密碼錯誤", Toast.LENGTH_SHORT).show();
        }

    }


}
