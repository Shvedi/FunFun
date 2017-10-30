package com.androidkurs.micke.i_had_fun;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by hadideknache on 2017-10-29.
 *
 *
 */
public class SplashActivty extends Activity {

    private  ImageView imageView,imgLogo;
    private TextView tvLogo;
    private RotateAnimation rotClockWise;
    private AnimationDrawable animationDrawable;
    private Animation fadeIn,fadeOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_animation);

        initComponents();
        InitListeners();
        startAnimations();
        startImageAnimation();

    }

    private void initComponents() {
        fadeIn = AnimationUtils.loadAnimation(this,R.anim.fade_in);
        fadeOut  = AnimationUtils.loadAnimation(this, R.anim.fade_out);

        imageView = (ImageView) findViewById(R.id.ivAnimation);
        imageView.setBackgroundResource(R.drawable.animation_list);
        animationDrawable = (AnimationDrawable) imageView.getBackground();

        imgLogo = (ImageView) findViewById(R.id.imageView);
        tvLogo = (TextView) findViewById(R.id.tvLogo);


    }

    private void InitListeners() {
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tvLogo.startAnimation(fadeIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tvLogo.startAnimation(fadeOut);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    private void startImageAnimation() {
        imageView.post(new Runnable() {
            @Override
            public void run() {
                animationDrawable.start();
            }
        });
        imageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setClass(SplashActivty.this, MainActivity.class);
                startActivity(intent);
                animationDrawable.stop();
                imgLogo.setAnimation(null);
                finish();
            }
        },2500);
    }

    private void startAnimations() {
        rotClockWise = new RotateAnimation(0f, 360f,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotClockWise.setInterpolator(new LinearInterpolator());
        rotClockWise.setRepeatCount(Animation.ABSOLUTE);
        rotClockWise.setDuration(1000);

        imgLogo.startAnimation(rotClockWise);
        tvLogo.startAnimation(fadeOut);
    }
}
