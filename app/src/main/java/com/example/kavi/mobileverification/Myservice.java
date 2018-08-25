package com.example.kavi.mobileverification;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.api.cache.http.HttpCachePolicy;
import com.apollographql.apollo.cache.http.ApolloHttpCache;
import com.apollographql.apollo.cache.http.DiskLruHttpCacheStore;
import com.apollographql.apollo.exception.ApolloException;

import java.io.File;

import javax.annotation.Nonnull;

import okhttp3.OkHttpClient;

/**
 * Created by kavi on 24/8/18.
 */

public class Myservice extends Service {

    public String mobileno;

    public ApolloClient apolloClient;

    public Integer money;
    public Integer moneyp;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
//        Toast.makeText(this, “Congrats! MyService Created”, Toast.LENGTH_LONG).show();
//        Log.d(TAG, “onCreate”);

        SharedPreferences sp = getSharedPreferences("Login", Context.MODE_PRIVATE);

        mobileno = sp.getString("mobile", null);



        File file = new File(this.getCacheDir().toURI());
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


//        callQuery();

        // setUpClient("https://digicashserver.herokuapp.com/graphql");

//        PersondetailsQuery persondetailsQuery = PersondetailsQuery.builder()
//                .build();


        apolloClient
                .query(PersondetailsQuery.builder().mobileno(mobileno).build())
                .httpCachePolicy(HttpCachePolicy.NETWORK_ONLY)
                .enqueue(new ApolloCall.Callback<PersondetailsQuery.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<PersondetailsQuery.Data> response) {


                        PersondetailsQuery.Data data = response.data();

                        if (data != null) {
                            Log.d("msg", "serviceeeeeeeeeeeeeeeeeeeeeee");
                        }

                        try {
                            moneyp = Integer.parseInt(data.person().get(0).wallet.toString());

                            Log.d("datas", Integer.toString(moneyp));

                            money = moneyp + 1;

                            Log.d("number", mobileno);

                            Log.d("wallet", Integer.toString(money));

                            UpdateMutation updateMutation = UpdateMutation.builder()

                                    .mobileno(mobileno)
                                    .wallet(money)
                                    .build();
                            ApolloCall<UpdateMutation.Data> call = apolloClient.mutate(updateMutation);
                            call.enqueue(new ApolloCall.Callback<UpdateMutation.Data>() {
                                @Override
                                public void onResponse(@Nonnull Response<UpdateMutation.Data> response) {
                                    UpdateMutation.Data res = response.data();

//                CustomPhoneStateListener.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        //  Toast.makeText(getApplication(), "User registered Successfully", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(CustomPhoneStateListener.this, money, Toast.LENGTH_LONG).show();
//
//                    }
//                });

                                    Log.d("done","donedonedone");
                                }

                                @Override
                                public void onFailure(@Nonnull ApolloException e) {
                                    Log.e("Fail", "onFailure: ", e);
                                }
                            });


//                            SharedPreferences sp = getSharedPreferences("Login", Context.MODE_PRIVATE);
//
//                            final SharedPreferences.Editor editor = sp.edit();
//
//                            editor.putString("mobile",mobno);
//                            editor.apply();

//                            Intent intent = new Intent(NavActivity.this, Cashout.class);
//                            intent.putExtra("number",mobno);
//                            startActivity(intent);

//                            Bundle bundle = new Bundle();
//                            bundle.putInt("balance", pwallet);
//                            bundle.putString("balance1",String.valueOf(pwallet));
//                            Cashout fragobj = new Cashout();
//                            fragobj.setArguments(bundle);

                        } catch (Exception e) {
                            Log.d("catch", "errrrrrrrrrrrrrrrrrrrrrrrr");
//                            postMutation(wallet,mobno,user);

//                            SharedPreferences sp = getSharedPreferences("Login", Context.MODE_PRIVATE);
//
//                            final SharedPreferences.Editor editor = sp.edit();
//
//                            editor.putString("mobile",mobno);
//                            editor.apply();

//                            Intent intent = new Intent(NavActivity.this, NavActivity.class);
//                            intent.putExtra("number",mobno);
//                            startActivity(intent);

                            // Log.d("pp",mobno +user+wallet);

                        }


                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {

                        Log.e("Fail", "onFailure: ", e);

                    }
                });





    }
}
