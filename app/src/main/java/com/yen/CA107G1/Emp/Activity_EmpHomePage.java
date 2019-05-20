package com.yen.CA107G1.Emp;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yen.CA107G1.R;
import com.yen.CA107G1.Server.CommonTask;
import com.yen.CA107G1.Server.ServerURL;

import java.util.concurrent.ExecutionException;

public class Activity_EmpHomePage extends AppCompatActivity {
    private ImageView empQrCodeScan, empCheckSendBack;
    private CommonTask checkINTask;
    private String result;
    private Boolean isCheckIN = false;
    private static final String PACKAGE = "com.google.zxing.client.android";

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

    public void findViews() {
        empQrCodeScan = findViewById(R.id.empQrCodeScan);
        empCheckSendBack = findViewById(R.id.empCheckSendBack);
        empQrCodeScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                try {
                    startActivityForResult(intent, 0);
                } catch (ActivityNotFoundException e) {
                    showDownloadDialog();
                }

            }
        });
    }

    private void showDownloadDialog() {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(this);
        downloadDialog.setTitle("No Scanner Found");
        downloadDialog.setMessage("Please download and install Scanner!");
        downloadDialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Uri uri = Uri.parse("market://search?q=pname:" + PACKAGE);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        try {
                            startActivity(intent);
                        } catch (ActivityNotFoundException ex) {
                            Log.e(ex.toString(),
                                    "Play Store is not installed; cannot install Barcode Scanner");
                        }
                    }
                });
        downloadDialog.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        downloadDialog.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == 0) {
            String contents = null;
            String message = "";
            if (resultCode == RESULT_OK) {
                contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                message = "Content: " + contents + "\nFormat: " + format;
            } else if (resultCode == RESULT_CANCELED) {
                message = "Scan was Cancelled!";
            }
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();


            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "checkIN");
            jsonObject.addProperty("h_ord_no", contents);

            try {
                checkINTask = new CommonTask(ServerURL.HotelOrder_URL, jsonObject.toString());
                result = checkINTask.execute().get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            isCheckIN = Boolean.valueOf(result);

            if (isCheckIN) {
                new AlertDialog.Builder(Activity_EmpHomePage.this)
                        .setTitle("系統提示")
                        .setMessage("成功CheckIn囉!")
                        .setIcon(R.drawable.ic_check).show();
            } else {
                new AlertDialog.Builder(Activity_EmpHomePage.this)
                        .setTitle("系統提示")
                        .setMessage("Oooooops!,發生錯誤囉,請重新掃描")
                        .setIcon(R.drawable.ic_error).show();
            }
        }
    }

}
