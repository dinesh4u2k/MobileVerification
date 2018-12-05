package com.example.kavi.mobileverification;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Nonnull;

import okhttp3.OkHttpClient;


public class CustomPhoneStateListener extends Activity implements View.OnTouchListener
{


    public String mobileno;
    final ArrayList<String> imagesFromURL = new ArrayList<String>();
    // private ViewGroup rootlayout;
    // private int xdelta;
    //private int ydelta;
    // float dX;
    // float dY;
    int lastAction;
    RelativeLayout root;

    private int xDelta;
    private int yDelta;
    public interface OnDragActionListener {
        /**
         * Called when drag event is started
         *
         * @param view The view dragged
         */
        void onDragStart(View view);

        /**
         * Called when drag event is completed
         *
         * @param view The view dragged
         */
        void onDragEnd(View view);
    }

    private View mView;
    private View mParent;
    private boolean isDragging;
    private boolean isInitialized = false;
    private int Listsize=0;
    private int width;
    private float xWhenAttached;
    private float maxLeft;
    private float maxRight;
    private float dX;
    public ApolloClient apolloClient;
    private int height;
    private float yWhenAttached;
    private float maxTop;
    private float maxBottom;
    private float dY;


    private OnDragActionListener mOnDragActionListener;

    public CustomPhoneStateListener()
    {

    }

    public CustomPhoneStateListener(View view) {
        this(view, (View) view.getParent(), null);
    }

    public CustomPhoneStateListener(View view, View parent) {
        this(view, parent, null);
    }

    public CustomPhoneStateListener(View view, OnDragActionListener onDragActionListener) {
        this(view, (View) view.getParent(), onDragActionListener);
    }

    public CustomPhoneStateListener(View view, View parent, OnDragActionListener onDragActionListener) {
        initListener(view, parent);
        setOnDragActionListener(onDragActionListener);
    }


    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Click Close button to get money", Toast.LENGTH_SHORT).show();
        //super.onBackPressed();

    }
    public void setOnDragActionListener(OnDragActionListener onDragActionListener) {
        mOnDragActionListener = onDragActionListener;
    }

    public void initListener(View view, View parent) {
        mView = view;
        mParent = parent;
        isDragging = false;
        isInitialized = false;
    }

    public void updateBounds() {
        updateViewBounds();
        updateParentBounds();
        isInitialized = true;
    }

    public void updateViewBounds() {
        width = mView.getWidth();
        xWhenAttached = mView.getX();
        dX = 0;

        height = mView.getHeight();
        yWhenAttached = mView.getY();
        dY = 0;
    }

    public void updateParentBounds() {
        maxLeft = 0;
        maxRight = maxLeft + mParent.getWidth();

        maxTop = 0;
        maxBottom = maxTop + mParent.getHeight();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (isDragging) {
            float[] bounds = new float[4];
            // LEFT
            bounds[0] = event.getRawX() + dX;
            if (bounds[0] < maxLeft) {
                bounds[0] = maxLeft;
            }
            // RIGHT
            bounds[2] = bounds[0] + width;
            if (bounds[2] > maxRight) {
                bounds[2] = maxRight;
                bounds[0] = bounds[2] - width;
            }
            // TOP
            bounds[1] = event.getRawY() + dY;
            if (bounds[1] < maxTop) {
                bounds[1] = maxTop;
            }
            // BOTTOM
            bounds[3] = bounds[1] + height;
            if (bounds[3] > maxBottom) {
                bounds[3] = maxBottom;
                bounds[1] = bounds[3] - height;
            }

            switch (event.getAction()) {
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    onDragFinish();
                    break;
                case MotionEvent.ACTION_MOVE:
                    mView.animate().x(bounds[0]).y(bounds[1]).setDuration(0).start();
                    break;
            }
            return true;
        } else {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isDragging = true;
                    if (!isInitialized) {
                        updateBounds();
                    }
                    dX = v.getX() - event.getRawX();
                    dY = v.getY() - event.getRawY();
                    if (mOnDragActionListener != null) {
                        mOnDragActionListener.onDragStart(mView);
                    }
                    return true;
            }
        }
        return false;
    }

    private void onDragFinish() {
        if (mOnDragActionListener != null) {
            mOnDragActionListener.onDragEnd(mView);
        }

        dX = 0;
        dY = 0;
        isDragging = false;
    }

    void callQuery()
    {


        File file = new File(getApplication().getCacheDir().toURI());
        //Size in bytes of the cache
        int size = 1024 * 1024;

        //Create the http response cache store
        DiskLruHttpCacheStore cacheStore = new DiskLruHttpCacheStore(file, size);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .build();

        apolloClient = ApolloClient.builder()
                .serverUrl("https://digicashserver.herokuapp.com/graphql")
                .httpCache(new ApolloHttpCache(cacheStore))
                .okHttpClient(okHttpClient)
                .build();


        apolloClient
                .query(ImgurlQuery.builder().build())
                .httpCachePolicy(HttpCachePolicy.NETWORK_FIRST)
                .enqueue(new ApolloCall.Callback<ImgurlQuery.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<ImgurlQuery.Data> response) {


//                        final String[] urll;

//                        SharedPreferences imgtopopup = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
//
//                        final SharedPreferences.Editor editorpop = imgtopopup.edit();


                        ImgurlQuery.Data data = response.data();
                        Listsize = response.data().banner().size();
//                        editorpop.putInt("listsize",Listsize);

                        for (int i = 0; i < Listsize; i++) {
//                            String var = String.valueOf(i);
//                            editorpop.putString(var,data.banner.get(i).imageurl.toString());
//                            urlll[i] = data.banner.get(i).imageurl.toString();
                            imagesFromURL.add(data.banner.get(i).imageurl.toString());
                            Log.d("datas111", imagesFromURL.toString());

                        }

                        Log.d("4444444444", imagesFromURL.get(3));

//                        Random rand = new Random();
//                        int n = rand.nextInt(Listsize);
//
//                        final String send = iurl[n];
//
//                        Log.i("url",send);
//
//                        System.out.println(send);

//
// editorpop.apply();

                        CustomPhoneStateListener.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }

                        });


                    }


                    @Override
                    public void onFailure(@Nonnull ApolloException e) {

                        Log.e("Fail", "onFailure: ", e);

                    }
                });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        callQuery();
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
        banner = findViewById(R.id.banner);

        //rootlayout = (ViewGroup) findViewById(R.id.view_root);
        //FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(1070,10000);
        //rootlayout.getHeight();
        //rootlayout.setLayoutParams(layoutParams);
        //rootlayout.setOnTouchListener(new ChoiceTouchListener());


        banner = findViewById(R.id.banner);

        SharedPreferences sp = getApplicationContext().getSharedPreferences("Login", Context.MODE_PRIVATE);

        Integer ls = sp.getInt("listsize", 0);

        for (int i = 0; i < ls; i++) {

            imagesFromURL.add(sp.getString("s"+i, null));


        }



       String final1 = imagesFromURL.get(n);



//        String url= getIntent().getStringExtra("url");

//        Toast.makeText(this, final1, Toast.LENGTH_LONG).show();

//        String um = "https://pbs.twimg.com/profile_images/874661809139073025/X8yzIhNy_400x400.jpg";

//        String url ="\""+um+"\"";

        Picasso.with(this)
                .load(final1)
                .fit()
                //.resize(400,300)                       // optional
                .into(banner);

        final DisplayMetrics dm = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;




        getWindow().setLayout((int) (width * 0.99), (int) (height));

        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);


        close = findViewById(R.id.close);
        close.getBackground().setAlpha(50);
        close.setTextSize(25);

        close.setTextColor(Color.BLACK);

//        Handler handler = new Handler();
//
//        handler.postDelayed(new Runnable() {
//            public void run() {
//                CustomPhoneStateListener.this.finish();
//            }
//        }, 10000);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CustomPhoneStateListener.this.finish();



            }
        });



        final View dragView = findViewById(R.id.root);
        //  dragView.setOnTouchListener(new CustomPhoneStateListener(mView));
//

        dragView.setOnTouchListener(new CustomPhoneStateListener(dragView));


        //dragView.setOnTouchListener(new CustomPhoneStateListener(mParent,mView));





    }




//
//    @Override
//    public boolean onTouch(View view, MotionEvent event) {
//        switch (event.getActionMasked()) {
//            case MotionEvent.ACTION_DOWN:
//                dX = view.getX() - event.getRawX();
//                dY = view.getY() - event.getRawY();
//                lastAction = MotionEvent.ACTION_DOWN;
//                break;
//
//            case MotionEvent.ACTION_MOVE:
//                view.setY(event.getRawY() + dY);
//                view.setX(event.getRawX() + dX);
//                lastAction = MotionEvent.ACTION_MOVE;
//                break;
//
//            case MotionEvent.ACTION_UP:
//                if (lastAction == MotionEvent.ACTION_DOWN)
//                    Toast.makeText(CustomPhoneStateListener.this, "Clicked!", Toast.LENGTH_SHORT).show();
//                break;
//
//            default:
//                return false;
//        }
//        return true;
//    }







   /* private final class ChoiceTouchListener implements View.OnTouchListener{
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
    }*/


}