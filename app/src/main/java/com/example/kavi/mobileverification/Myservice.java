package com.example.kavi.mobileverification;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
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


   // First of all, create a service class that will run a handler after a given time interval,



        private Handler mHandler;
        // default interval for syncing data
        public static final long DEFAULT_SYNC_INTERVAL = 30 * 1000;

        // task to be run here
        private Runnable runnableService = new Runnable() {
            @Override
            public void run() {
                syncData();
                // Repeat this runnable code block again every ... min
                mHandler.postDelayed(runnableService, DEFAULT_SYNC_INTERVAL);
            }
        };

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            // Create the Handler object
            mHandler = new Handler();
            // Execute a runnable task as soon as possible
            mHandler.post(runnableService);

            return START_STICKY;
        }

        private synchronized void syncData() {
            // call your rest service here
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

        apolloClient
                .query(PersondetailsQuery.builder().mobileno(mobileno).build())
                .httpCachePolicy(HttpCachePolicy.CACHE_FIRST)
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

                            money=moneyp + 1;

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
                          Log.d("done","donedonedone");

                                }

                                @Override
                                public void onFailure(@Nonnull ApolloException e) {
                                    Log.e("Fail", "onFailure: ", e);
                                }
                            });



                        } catch (Exception e) {
                            Log.d("catch", "errrrrrrrrrrrrrrrrrrrrrrrr");


                        }


                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {

                        Log.e("Fail", "onFailure: ", e);

                    }
                });



        }


//    @Override
//    public void onCreate() {
//
//
//        SharedPreferences sp = getSharedPreferences("Login", Context.MODE_PRIVATE);
//
//        mobileno = sp.getString("mobile", null);
//
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
//        apolloClient
//                .query(PersondetailsQuery.builder().mobileno(mobileno).build())
//                .httpCachePolicy(HttpCachePolicy.CACHE_FIRST)
//                .enqueue(new ApolloCall.Callback<PersondetailsQuery.Data>() {
//                    @Override
//                    public void onResponse(@Nonnull Response<PersondetailsQuery.Data> response) {
//
//
//                        PersondetailsQuery.Data data = response.data();
//
//                        if (data != null) {
//                            Log.d("msg", "serviceeeeeeeeeeeeeeeeeeeeeee");
//                        }
//
//                        try {
//                            moneyp = Integer.parseInt(data.person().get(0).wallet.toString());
//
//                            Log.d("datas", Integer.toString(moneyp));
//
//                            money=moneyp + 1;
//
//                            Log.d("number", mobileno);
//
//                            Log.d("wallet", Integer.toString(money));
//
//                            UpdateMutation updateMutation = UpdateMutation.builder()
//
//                                    .mobileno(mobileno)
//                                    .wallet(money)
//                                    .build();
//                            ApolloCall<UpdateMutation.Data> call = apolloClient.mutate(updateMutation);
//                            call.enqueue(new ApolloCall.Callback<UpdateMutation.Data>() {
//                                @Override
//                                public void onResponse(@Nonnull Response<UpdateMutation.Data> response) {
//                                    UpdateMutation.Data res = response.data();
//                          Log.d("done","donedonedone");
//                                }
//
//                                @Override
//                                public void onFailure(@Nonnull ApolloException e) {
//                                    Log.e("Fail", "onFailure: ", e);
//                                }
//                            });
//
//
//
//                        } catch (Exception e) {
//                            Log.d("catch", "errrrrrrrrrrrrrrrrrrrrrrrr");
//
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




    }

