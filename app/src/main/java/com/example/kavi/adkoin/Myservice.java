package com.example.kavi.adkoin;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.api.cache.http.HttpCachePolicy;
import com.apollographql.apollo.cache.http.ApolloHttpCache;
import com.apollographql.apollo.cache.http.DiskLruHttpCacheStore;
import com.apollographql.apollo.exception.ApolloException;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.annotation.Nonnull;

import okhttp3.OkHttpClient;

public class Myservice extends Service {

    public String  callcount;
    public int convert;
    public int number;

    static final String CONNECTIVITY_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    public BroadcastReceiver receiver;

    public String mobileno;

    public ApolloClient apolloClient;

    public Integer money;
    public Integer moneyp;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


        private Handler mHandler;

        private Runnable runnableService = new Runnable() {
            @Override
            public void run() {
                syncData();

            }
        };
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        startForeground(3,new Notification());
//    }

    @Override
        public int onStartCommand(Intent intent, int flags, int startId) {

            //Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if (CONNECTIVITY_CHANGE_ACTION.equals(action)) {
                        //check internet connection
                        if (!ConnectionHelper.isConnectedOrConnecting(context)) {
                            if (context != null) {
                                boolean show = false;
                                if (ConnectionHelper.lastNoConnectionTs == -1) {//first time
                                    show = true;
                                    ConnectionHelper.lastNoConnectionTs = System.currentTimeMillis();
                                } else {
                                    if (System.currentTimeMillis() - ConnectionHelper.lastNoConnectionTs > 1000) {
                                        show = true;
                                        ConnectionHelper.lastNoConnectionTs = System.currentTimeMillis();
                                    }
                                }

                                if (show && ConnectionHelper.isOnline) {
                                    ConnectionHelper.isOnline = false;
                                    Log.i("NETWORK123","Connection lost");
                                    //manager.cancelAll();
                                }
                            }
                        } else {
                            Log.i("NETWORK123","Connected");
//                            showNotifications("APP" , "It is working");
                            mHandler = new Handler();
                            // Execute a runnable task as soon as possible
                            mHandler.post(runnableService);

                            // Perform your actions here
                            ConnectionHelper.isOnline = true;
                        }
                    }
                }
            };
            registerReceiver(receiver,filter);

//            unregisterReceiver(receiver);


//            onTaskRemoved(intent);
            // Create the Handler object


            return START_STICKY;
        }


        void getcountQuery(){

            SharedPreferences sp = getApplication().getSharedPreferences("Login", Context.MODE_PRIVATE);
            final String mobile = sp.getString("mobile", null);

            File file = new File(getApplication().getCacheDir().toURI());
            //Size in bytes of the cache
            int size = 1024 * 1024;

            //Create the http response cache store
            DiskLruHttpCacheStore cacheStore = new DiskLruHttpCacheStore(file, size);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .build();

            apolloClient = ApolloClient.builder()
                    .serverUrl("https://adkoin-server.herokuapp.com/graphql")
                    .httpCache(new ApolloHttpCache(cacheStore))
                    .okHttpClient(okHttpClient)
                    .build();


            apolloClient
                    .query(PersondetailsQuery.builder().mobileno(mobile).build())
                    .httpCachePolicy(HttpCachePolicy.NETWORK_FIRST)
                    .enqueue(new ApolloCall.Callback<PersondetailsQuery.Data>() {
                        @Override
                        public void onResponse(@Nonnull Response<PersondetailsQuery.Data> response) {


                            PersondetailsQuery.Data data1 = response.data();
                            callcount =data1.person.get(0).count.toString();
                            convert = Integer.valueOf(callcount);


                        }


                        @Override
                        public void onFailure(@Nonnull ApolloException e) {

                            Log.e("Fail", "onFailure: ", e);

                        }
                    });


        }



        private synchronized void syncData() {

            getcountQuery();
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
                .serverUrl("https://adkoin-server.herokuapp.com/graphql")
                .httpCache(new ApolloHttpCache(cacheStore))
                .okHttpClient(okHttpClient)
                .build();

        apolloClient
                .query(PersondetailsQuery.builder().mobileno(mobileno).build())
                .httpCachePolicy(HttpCachePolicy.NETWORK_FIRST)
                .enqueue(new ApolloCall.Callback<PersondetailsQuery.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<PersondetailsQuery.Data> response) {


                        PersondetailsQuery.Data data = response.data();

                        if (data != null) {
                            Log.d("msg", "serviceeeeeeeeeeeeeeeeeeeeeee");
                        }

                        try {



                            if (convert == 0){
                                convert =1;

                                UpdateCallMutation updateCallMutation = UpdateCallMutation.builder()

                                        .mobileno(mobileno)
                                        .count(convert)
                                        .build();
                                ApolloCall<UpdateCallMutation.Data> call = apolloClient.mutate(updateCallMutation);
                                call.enqueue(new ApolloCall.Callback<UpdateCallMutation.Data>() {
                                    @Override
                                    public void onResponse(@Nonnull Response<UpdateCallMutation.Data> response) {
                                        UpdateCallMutation.Data res = response.data();
                                        Log.d("done",String.valueOf(res));

                                    }

                                    @Override
                                    public void onFailure(@Nonnull ApolloException e) {
                                        Log.e("Fail", "onFailure: ", e);
                                    }
                                });





                            }else if(convert==30) {

                                Log.d("cc","level reached");

                            }
//                            else {
//                                Calendar calendar = Calendar.getInstance();
//                                SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm");
//                                String strDate = mdformat.format(calendar.getTime());
//                                System.out.println(strDate);
//                                int a = calendar.get(Calendar.AM_PM);
//                                System.out.println(a);
//                                System.out.println(Calendar.PM);
//
//                                if(a == Calendar.PM) {
//                                    if (strDate == "24:00") {
//                                        System.out.println("12 midnight");
//                                        convert = 0;
//                                        UpdateCallMutation updateCallMutation = UpdateCallMutation.builder()
//
//                                                .mobileno(mobileno)
//                                                .count(convert)
//                                                .build();
//                                        ApolloCall<UpdateCallMutation.Data> call = apolloClient.mutate(updateCallMutation);
//                                        call.enqueue(new ApolloCall.Callback<UpdateCallMutation.Data>() {
//                                            @Override
//                                            public void onResponse(@Nonnull Response<UpdateCallMutation.Data> response) {
//                                                UpdateCallMutation.Data res = response.data();
//                                                Log.d("done", String.valueOf(res));
//
//                                            }
//
//                                            @Override
//                                            public void onFailure(@Nonnull ApolloException e) {
//                                                Log.e("Fail", "onFailure: ", e);
//                                            }
//                                        });
//
//                                    }
//                                }else {


//                                }

//                            }

                            number = convert + 1;
                            UpdateCallMutation updateCallMutation = UpdateCallMutation.builder()

                                    .mobileno(mobileno)
                                    .count(number)
                                    .build();
                            ApolloCall<UpdateCallMutation.Data> call = apolloClient.mutate(updateCallMutation);
                            call.enqueue(new ApolloCall.Callback<UpdateCallMutation.Data>() {
                                @Override
                                public void onResponse(@Nonnull Response<UpdateCallMutation.Data> response) {
                                    UpdateCallMutation.Data res = response.data();
                                    Log.d("done", String.valueOf(res));

                                }

                                @Override
                                public void onFailure(@Nonnull ApolloException e) {
                                    Log.e("Fail", "onFailure: ", e);
                                }
                            });

                            moneyp = Integer.parseInt(data.person().get(0).wallet.toString());

                            Log.d("datas", Integer.toString(moneyp));

                            money = moneyp + 1;

                            Log.d("number", mobileno);

                            Log.d("wallet", Integer.toString(money));

                            UpdateMutation updateMutation = UpdateMutation.builder()

                                    .mobileno(mobileno)
                                    .wallet(money)
                                    .build();
                            ApolloCall<UpdateMutation.Data> call1 = apolloClient.mutate(updateMutation);
                            call1.enqueue(new ApolloCall.Callback<UpdateMutation.Data>() {
                                @Override
                                public void onResponse(@Nonnull Response<UpdateMutation.Data> response) {
                                    UpdateMutation.Data res = response.data();
                          Log.d("done",String.valueOf(res));

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





    @Override
    public void onDestroy() {
            unregisterReceiver(receiver);
        super.onDestroy();
//        stopForeground(true);
//        stopSelf();

    }



    }

