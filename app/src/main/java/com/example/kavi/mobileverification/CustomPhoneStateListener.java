package com.example.kavi.mobileverification;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.api.cache.http.HttpCachePolicy;
import com.apollographql.apollo.cache.http.ApolloHttpCache;
import com.apollographql.apollo.cache.http.DiskLruHttpCacheStore;
import com.apollographql.apollo.exception.ApolloException;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Random;

import javax.annotation.Nonnull;

import okhttp3.OkHttpClient;


public class CustomPhoneStateListener extends Activity {


    public String mobileno;
    private ViewGroup rootlayout;
    private int xdelta;
    private int ydelta;


    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Click Close button to get money", Toast.LENGTH_SHORT).show();
        //super.onBackPressed();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        Button close;
        ImageView banner;


        int images[] = {
                /*"https://pbs.twimg.com/profile_images/874661809139073025/X8yzIhNy_400x400.jpg",
                "https://storage.googleapis.com/gd-wagtail-prod-assets/original_images/evolving_google_identity_share.jpg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSbC24wxj7HNxqtewPrOMHcbYG-TgBBqe_xPHUa8CmVuoBqGneU",
                "https://cdn.mos.cms.futurecdn.net/2sDGvXSwDRvrJqq9YN5oc4-480-80.jpg"*/
                R.drawable.im1,
                R.drawable.im2,
                R.drawable.im3,
                R.drawable.im4,
                R.drawable.im5,
//                R.drawable.banner1


        };

        Random rand = new Random();
        int n = rand.nextInt(5);


        super.onCreate(savedInstanceState);


        setContentView(R.layout.popup);
        rootlayout = (ViewGroup) findViewById(R.id.view_root);
        banner = findViewById(R.id.banner);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(1070,10000);
        //rootlayout.getHeight();
        rootlayout.setLayoutParams(layoutParams);
        //rootlayout.setOnTouchListener(new ChoiceTouchListener());



        banner = findViewById(R.id.banner);

        Picasso.with(this)
                .load(images[n])
                .fit()
                //.resize(400,300)                       // optional
                .into(banner);

        final DisplayMetrics dm = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;


        getWindow().setLayout((int) (width * 0.99), (int) (height));

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);


        close = findViewById(R.id.close);
        close.getBackground().setAlpha(50);
        close.setTextSize(25);

        close.setTextColor(Color.BLACK);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CustomPhoneStateListener.this.finish();
               // startService(new Intent(getApplication(), Myservice.class));

//                finishAffinity();


            }
        });



    }

    private final class ChoiceTouchListener implements View.OnTouchListener{

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            final int X = (int) motionEvent.getRawX();
            final int Y = (int) motionEvent.getRawY();
            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK){
                case MotionEvent.ACTION_DOWN:

                    FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) view.getLayoutParams();
                    xdelta = X - lParams.leftMargin;
                    ydelta = Y - lParams.topMargin;
                    break;

                case MotionEvent.ACTION_UP:
                    break;


                case MotionEvent.ACTION_POINTER_DOWN:
                    break;

                case MotionEvent.ACTION_POINTER_UP:
                    break;

                case MotionEvent.ACTION_MOVE:
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();


                    //layoutParams.leftMargin = X - xdelta;
                    layoutParams.topMargin = Y - ydelta;

                    //layoutParams.rightMargin = -250;
                    layoutParams.bottomMargin = -250;
                    view.setLayoutParams(layoutParams);
                    break;
            }

            rootlayout.invalidate();
            return true;
        }
    }



}








