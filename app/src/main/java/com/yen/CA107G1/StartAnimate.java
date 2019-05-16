package com.yen.CA107G1;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class StartAnimate extends AppCompatActivity {
    private ImageView logo_home, logo_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_animate);

        findViews();

    }

    public void findViews() {
        logo_home = findViewById(R.id.logo_home);
        logo_text = findViewById(R.id.logo_text);

        AlphaAnimation alphaAnimation = getAlphaAnimation();
        alphaAnimation.setInterpolator(AnimationUtils.loadInterpolator(StartAnimate.this, android.R.anim.overshoot_interpolator));
        logo_text.startAnimation(alphaAnimation);

        getTranslateAnimation();


    }

    public AlphaAnimation getAlphaAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(2000);
        alphaAnimation.setRepeatMode(Animation.RESTART);
        alphaAnimation.setRepeatMode(Animation.INFINITE);
        return alphaAnimation;
    }

    private TranslateAnimation getTranslateAnimation() {
        View parentView = (View) logo_home.getParent();
        // 球移動的距離
        float distance = parentView.getHeight() - parentView.getPaddingTop() -
                parentView.getPaddingTop() - logo_home.getHeight();
        TranslateAnimation translateAnimation = new TranslateAnimation(0, distance, 0, 0);
        translateAnimation.setDuration(2000);
        translateAnimation.setRepeatMode(Animation.RESTART);
        translateAnimation.setRepeatCount(Animation.INFINITE);
        return translateAnimation;
    }

    public void intent(){
        Intent intent = new Intent(this, Activity_Main.class);
        startActivity(intent);
        finish();
    }
}
