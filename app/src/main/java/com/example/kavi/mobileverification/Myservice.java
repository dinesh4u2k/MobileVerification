package com.example.kavi.mobileverification;

import android.app.Notification;
import android.app.NotificationManager;
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

    public Integer callcount;

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


   // First of all, create a service class that will run a handler after a given time interval,



        private Handler mHandler;
        // default interval for syncing data
//        public static final long DEFAULT_SYNC_INTERVAL = 30 * 1000;

        // task to be run here
        private Runnable runnableService = new Runnable() {
            @Override
            public void run() {
                syncData();
                // Repeat this runnable code block again every ... min
//                mHandler.postDelayed(runnableService, DEFAULT_SYNC_INTERVAL);
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
                .httpCachePolicy(HttpCachePolicy.NETWORK_FIRST)
                .enqueue(new ApolloCall.Callback<PersondetailsQuery.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<PersondetailsQuery.Data> response) {


                        PersondetailsQuery.Data data = response.data();

                        if (data != null) {
                            Log.d("msg", "serviceeeeeeeeeeeeeeeeeeeeeee");
                        }

                        try {
                            SharedPreferences spbroad = getSharedPreferences("cc", Context.MODE_PRIVATE);
                            final SharedPreferences.Editor editor = spbroad.edit();

                            callcount = spbroad.getInt("count",0);

                            if (callcount == 0){
                                callcount =1;
                                editor.putInt("count",callcount);
                                editor.apply();

                            }else if(callcount==30) {

                                Log.d("cc","level reached");
                            }else {

                                callcount=callcount+1;
                                editor.putInt("count",callcount);
                                editor.apply();

                            }

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

