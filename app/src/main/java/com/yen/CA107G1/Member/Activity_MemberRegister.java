package com.yen.CA107G1.Member;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yen.CA107G1.R;
import com.yen.CA107G1.Util.Util;
import com.yen.CA107G1.Server.CommonTask;
import com.yen.CA107G1.Server.ServerURL;

import java.util.concurrent.ExecutionException;

public class Activity_MemberRegister extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_register);

        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://10.120.26.6:8081/CA107G1_20190507/front_end/member/register.jsp");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            // 在7.0時deprecated(還是有支援)，為了向下版本支援仍用此方法
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

    }
}
