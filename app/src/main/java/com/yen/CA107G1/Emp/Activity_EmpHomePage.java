package com.yen.CA107G1.Emp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.yen.CA107G1.R;

public class Activity_EmpHomePage extends AppCompatActivity {
    private ImageView empQrCodeScan, empCheckSendBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_home_page);

        findViews();
        empCheckSendBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_EmpHomePage.this, Activity_EmpToDayOrder.class);
                startActivity(intent);
            }
        });
    }

    public void findViews(){
        empQrCodeScan = findViewById(R.id.empQrCodeScan);
        empCheckSendBack = findViewById(R.id.empCheckSendBack);
    }



}
