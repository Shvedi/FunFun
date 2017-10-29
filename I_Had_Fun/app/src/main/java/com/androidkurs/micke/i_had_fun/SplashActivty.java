package com.androidkurs.micke.i_had_fun;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

/**
 * Created by hadideknache on 2017-10-29.
 *
 *
 */

public class SplashActivty extends Activity {

    private  ImageView imageView,imgLogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_animation);

        RotateAnimation anim = new RotateAnimation(0f, 360f,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.ABSOLUTE);
        anim.setDuration(1000);


        imgLogo = (ImageView) findViewById(R.id.imageView);
        imgLogo.startAnimation(anim);


        imageView = (ImageView) findViewById(R.id.ivAnimation);
        imageView.setBackgroundResource(R.drawable.animation_list);
        final AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
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
}
