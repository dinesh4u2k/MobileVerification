package com.example.kavi.mobileverification;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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

    public Integer money;
    public Integer moneyp;

    public String mobileno;

    public ApolloClient apolloClient;

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
                R.drawable.im5


        };

        Random rand = new Random();
        int n = rand.nextInt(5);

        //try {
//            requestWindowFeature(Window.FEATURE_NO_TITLE);
//            this.setFinishOnTouchOutside(false);
//            Log.d("flag2", "flag2");

        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//            getWindow().addFlags(
//                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

//            Log.d("flagy ", "flagy");

        setContentView(R.layout.popup);

//            Log.d("flagz ", "flagz");

//            String number = getIntent().getStringExtra(
//                    TelephonyManager.EXTRA_INCOMING_NUMBER);
//            TextView text = (TextView) findViewById(R.id.text1);
//            text.setText("Incoming call from " + number);


        banner = findViewById(R.id.banner);

        Picasso.with(this)
                .load(images[n])
                .fit()
                //.resize(400,300)                       // optional
                .into(banner);

        final DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        //getWindow().getWindowManager().getDefaultDisplay();

        getWindow().setLayout((int) (width * .9), (int) (height * .4));
        //  getWindow().setBackgroundDrawable(null);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |

                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        close = findViewById(R.id.close);
        close.getBackground().setAlpha(50);
        close.setTextSize(25);
//
        close.setTextColor(Color.BLACK);

//        final SharedPreferences sp = getSharedPreferences("money", Context.MODE_PRIVATE);
//
//        final SharedPreferences.Editor editor = sp.edit();
//
//        editor.putInt("money",0);
//        editor.apply();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SharedPreferences sp = getSharedPreferences("Login", Context.MODE_PRIVATE);
//
//                mobileno = sp.getString("mobile", null);

               // callQuery(mobileno);


                //postMutation(mobileno,money);

                startService(new Intent(getApplication(), Myservice.class));

                finishAffinity();

//               money = sp.getInt("money",0);
//
//                moneyp= money+1;
//
//                editor.putInt("money",moneyp);
//                editor.apply();

//                ActivityCompat.finishAffinity(CustomPhoneStateListener.this);
            }
        });


        // }
//        catch (Exception e) {
//            Log.d("Exception", e.toString());
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

    }

  //  void callQuery(final String mobno) {

//        final String phonenumber1 = getIntent().getStringExtra("phonenumber1");
//
//        final String username = getIntent().getStringExtra("username");

//        File file = new File(this.getCacheDir().toURI());
//        //Size in bytes of the cache
//        int size = 1024 * 1024;
//
//        //Create the http response cache store
//        DiskLruHttpCacheStore cacheStore = new DiskLruHttpCacheStore(file, size);
//
//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .build();
//
//        apolloClient = ApolloClient.builder()
//                .serverUrl("https://digicashserver.herokuapp.com/graphql")
//                .httpCache(new ApolloHttpCache(cacheStore))
//                .okHttpClient(okHttpClient)
//                .build();
//
//
////        callQuery();
//
//        // setUpClient("https://digicashserver.herokuapp.com/graphql");
//
////        PersondetailsQuery persondetailsQuery = PersondetailsQuery.builder()
////                .build();
//
//
//        apolloClient
//                .query(PersondetailsQuery.builder().mobileno(mobno).build())
//                .httpCachePolicy(HttpCachePolicy.NETWORK_ONLY)
//                .enqueue(new ApolloCall.Callback<PersondetailsQuery.Data>() {
//                    @Override
//                    public void onResponse(@Nonnull Response<PersondetailsQuery.Data> response) {
//
//
//                        PersondetailsQuery.Data data = response.data();
//
//                        if (data != null) {
//                            Log.d("msg", "kkkkkkkkkkkkkkasdddddddddddddddddddddddddddddd");
//                        }
//
//                        try {
//                            moneyp = Integer.parseInt(data.person().get(0).wallet.toString());
//
//                            Log.d("datas", Integer.toString(moneyp));
//
//                            money = moneyp + 1;
//
//                            Log.d("number", mobileno);
//
//                            Log.d("wallet", Integer.toString(money));
//
//
////                            SharedPreferences sp = getSharedPreferences("Login", Context.MODE_PRIVATE);
////
////                            final SharedPreferences.Editor editor = sp.edit();
////
////                            editor.putString("mobile",mobno);
////                            editor.apply();
//
////                            Intent intent = new Intent(NavActivity.this, Cashout.class);
////                            intent.putExtra("number",mobno);
////                            startActivity(intent);
//
////                            Bundle bundle = new Bundle();
////                            bundle.putInt("balance", pwallet);
////                            bundle.putString("balance1",String.valueOf(pwallet));
////                            Cashout fragobj = new Cashout();
////                            fragobj.setArguments(bundle);
//
//                        } catch (Exception e) {
//                            Log.d("catch", "errrrrrrrrrrrrrrrrrrrrrrrr");
////                            postMutation(wallet,mobno,user);
//
////                            SharedPreferences sp = getSharedPreferences("Login", Context.MODE_PRIVATE);
////
////                            final SharedPreferences.Editor editor = sp.edit();
////
////                            editor.putString("mobile",mobno);
////                            editor.apply();
//
////                            Intent intent = new Intent(NavActivity.this, NavActivity.class);
////                            intent.putExtra("number",mobno);
////                            startActivity(intent);
//
//                            // Log.d("pp",mobno +user+wallet);
//
//                        }
//
//
//                    }
//
//                    @Override
//                    public void onFailure(@Nonnull ApolloException e) {
//
//                        Log.e("Fail", "onFailure: ", e);
//
//                    }
//                });
//
//
//        UpdateMutation updateMutation = UpdateMutation.builder()
//
//                .mobileno(mobno)
//                .wallet(money)
//                .build();
//        ApolloCall<UpdateMutation.Data> call = apolloClient.mutate(updateMutation);
//        call.enqueue(new ApolloCall.Callback<UpdateMutation.Data>() {
//            @Override
//            public void onResponse(@Nonnull Response<UpdateMutation.Data> response) {
//                UpdateMutation.Data res = response.data();
//
//                CustomPhoneStateListener.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        //  Toast.makeText(getApplication(), "User registered Successfully", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(CustomPhoneStateListener.this, money, Toast.LENGTH_LONG).show();
//
//                    }
//                });
//            }
//
//            @Override
//            public void onFailure(@Nonnull ApolloException e) {
//                Log.e("Fail", "onFailure: ", e);
//            }
//        });
//    }

}








